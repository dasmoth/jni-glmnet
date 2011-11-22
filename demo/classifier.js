var canvas;
var points = [];

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

function init() {
    canvas = document.getElementById('classifier_view');
    var g = canvas.getContext('2d');
    g.fillStyle = 'red';
    g.fillRect(100, 100, 100, 100);

    canvas.addEventListener('mousedown', function(ev) {
	var br = canvas.getBoundingClientRect();
	var x = ev.clientX - br.left, y = ev.clientY - br.top;

	var type = document.getElementById('ptA').checked;
	points.push(new Point(x, y, type));

	if (type) {
	    g.fillStyle = 'black';
	} else {
	    g.fillStyle = 'blue';
	}
	g.fillRect(x - 2, y - 2, 4, 4);
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

	alert(JSON.stringify({x: x, y: y}));
    }, false);
}