var canvas;
var points = [];
var model = null;
var gradient = null;

function Point(x, y, label) {
    this.x = x;
    this.y = y;
    this.label = label;
}

function RadialBasis(x, y, r) {
    this.x = x;
    this.y = y;
    this.r = r;
}

RadialBasis.prototype.score = function(p) {
    var x = p.x, y = p.y;
    var d = (x-this.x)*(x-this.x) + (y-this.y)*(y-this.y);
    return Math.exp(-d*this.r*this.r);
}


function logit(x) {
    return 1.0 / (1.0 + Math.exp(-x));
}

function init() {
    canvas = document.getElementById('classifier_view');
    var g = canvas.getContext('2d');

    gradient = makeGradient(100, 'blue', 'white', 'red');

    canvas.addEventListener('mousedown', function(ev) {
	var br = canvas.getBoundingClientRect();
	var x = ev.clientX - br.left, y = ev.clientY - br.top;

	var type = document.getElementById('ptA').checked;
	var p = new Point(x, y, type);
	points.push(new Point(x, y, type));

	if (model) {
	    model = null;
	    draw();
	} else {
	    drawPoint(g, p);
	}
    }, false);

    document.getElementById('classify').addEventListener('mousedown', function(ev) {
	var bases = [];
	for (var pi = 0; pi < points.length; ++pi) {
	    var p = points[pi];
	    bases.push(new RadialBasis(p.x, p.y, 0.01));
	}

	var x = [], y=[];
	for (var pi = 0; pi < points.length; ++pi) {
	    var p = points[pi];
	    var preds = [];
	    for (var bi = 0; bi < bases.length; ++bi) {
		var b = bases[bi];
		preds.push(b.score(p));
	    }
	    x.push(preds); y.push(p.label);
	}

	var req = new XMLHttpRequest();
	req.open('POST', '/v1/classify', true);
	req.onreadystatechange = function() {
	    if (req.readyState == 4 && req.status == 200) {
		// alert(req.responseText);
		resp = JSON.parse(req.responseText);
		model = {bases: bases, weights: resp.weights, intercept: resp.intercept};
		draw();
	    }
	};
	req.send(JSON.stringify({x: x, y: y}));
    }, false);

    document.getElementById('clear').addEventListener('mousedown', function(ev) {
	model = null;
	points = [];
	draw();
    }, false);
}

function draw() {
    canvas = document.getElementById('classifier_view');
    var w = canvas.getAttribute('width')|0;
    var h = canvas.getAttribute('height')|0;
    var g = canvas.getContext('2d');
    var rez = 3;
    var halfRez = rez/2;

    if (model) {
	for (var x = 0; x < w; x += rez) {
	    for (var y = 0; y < h; y += rez) {
		var z = model.intercept;
		var p = new Point(x + halfRez, y + halfRez);
		for (var bi = 0; bi < model.bases.length; ++bi) {
		    if (model.weights[bi] != 0) {
			z += model.weights[bi] * model.bases[bi].score(p);
		    }
		}
		var ly = logit(z);
		g.fillStyle = gradient[(ly*(gradient.length - 1))|0];
		g.fillRect(x, y, rez, rez);
	    }
	}
    } else {
	g.fillStyle = 'white';
	g.fillRect(0, 0, w, h);
    }
    
    for (var pi = 0; pi < points.length; ++pi) {
	var p = points[pi];
	if (model && model.weights[pi] != 0) {
	    drawPoint(g, p, 'rgb(0,255,0)');
	} else {
	    drawPoint(g, p);
	}
    }
}

function drawPoint(g, p, c) {
    g.fillStyle = c || 'black';

    if (p.label) {
	g.fillRect(p.x - 3, p.y - 3, 6, 6);
    } else {
	g.beginPath();
	g.arc(p.x, p.y, 4, 0, 5);
	g.fill();
    }
}