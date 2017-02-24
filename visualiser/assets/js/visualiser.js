

var regExp = /\(([+-]?([0-9]*[.])?[0-9]+),([+-]?([0-9]*[.])?[0-9]+)\)/g; 
var offSet = 25;

var inputString, solutionString;
var caseSelected;
var solutionRobots = [];
var robotString=[], obstacleString=[], paths=[];

var scalingFactor = 1.0;
var maxPosX=0, minPosX=0, maxPosY=0, minPosY=0;
var pathFlag = false;

var colourList = ["#2980B9", "#EA4C88",  "#9B59B6", "#F39C12", "#C0392B", "#95A5A6", "#95A5A6","#16A085"]

// RAYMOND
var showRobotIndices = false;

//--objects
function point(point) {
	this.x = point.split(",")[0];
	this.y = point.split(",")[1];
}

function obstacle() {
	this.points = [];
	this.add = function (obj, pt) {
		var tmp = new point(pt);
		obj.points.push(tmp);
	}
}

function robot(id){
	this.id = id;
	this.points = [];

	//awake = 1, asleep = 0
	this.state = false;
	this.colour = "#000000";

	this.currentPosition;

	//stepCounter
	this.stepCount = 0;

	//given an instruction stream create an array of points
	this.add = function (obj, pt) {
		//take out outer "(" and ")"
		//pt = pt.slice(1,-1);
		// console.log(pt.charCodeAt(pt.length-1));
		//split to x,y
		pt = pt.match(regExp);
		for(var i=0; i<pt.length; i++){
			pt[i] = pt[i].slice(1, -1);
		}

		var i;
		for (i=0; i<pt.length; i++){
			obj.points.push(new point(pt[i]))
		}

		obj.currentPosition = obj.points[0];
	}

	this.nextStep = function (obj) {
	    var ctx = canvas.getContext('2d'); 

		if (obj.state && obj.stepCount < obj.points.length+1) {
			var pt = obj.points[obj.stepCount+1];
			//draw line from old to new
			if(pt != undefined) {
				ctx.beginPath();
				var startX = ((obj.points[obj.stepCount].x-minPosX)*scalingFactor)+offSet;
				var startY = ((obj.points[obj.stepCount].y-minPosY)*scalingFactor)+offSet;

				var endX = ((obj.points[obj.stepCount+1].x-minPosX)*scalingFactor)+offSet;
				var endY = ((obj.points[obj.stepCount+1].y-minPosY)*scalingFactor)+offSet;

				ctx.moveTo(startX , startY);
				ctx.lineTo(endX , endY);

				ctx.strokeStyle = obj.colour;
				ctx.stroke();
				ctx.closePath();


				console.log("robot: " + obj.id + " from " + obj.points[obj.stepCount].x + ',' + obj.points[obj.stepCount].y + " to " + obj.points[obj.stepCount+1].x + ',' + obj.points[obj.stepCount+1].y);

				obj.currentPosition = obj.points[obj.stepCount+1];
			}
			if(obj.stepCount < obj.points.length - 1){
				obj.stepCount += 1;
			}
		}
		else {
			// console.log("robot not awake");
		}
	}


	/*this.prevStep = function (obj) {
		if (obj.state) {
			var ret = obj.points[obj.stepCount];
			obj.stepCount -= 1;
			return ret;
		}
		else {
			return 0;
		}
	}*/

	this.checkAwake = function(obj) {
		//if asleep
		if (!obj.state) {
		    var ctx = canvas.getContext('2d'); 
		    //console.log(solutionRobots.length)
			var i;
			for (i=0; i<solutionRobots.length;i++){
				//for each robot check if awake wheter their position is same as mine. If so awaken;
				if (solutionRobots[i].state) {
					if (solutionRobots[i].currentPosition.x == obj.currentPosition.x && solutionRobots[i].currentPosition.y == obj.currentPosition.y) {
						console.log("robot " + solutionRobots[i].id + " woke robot " + obj.id + " at " + obj.currentPosition.x + ',' + obj.currentPosition.y);
						
						ctx.beginPath();
						ctx.arc((obj.currentPosition.x- minPosX)*scalingFactor+offSet , (obj.currentPosition.y-minPosY)*scalingFactor+offSet , 5, 0, 2*Math.PI);
						ctx.fillStyle = obj.colour;
						ctx.fill();
						ctx.strokeStyle = "#FFFF00"
						ctx.stroke();
						ctx.closePath();

						obj.state = true;
					}
				}
			}
		}
	}
}


function calculateSolution(){
	var index = document.getElementById("caseSelectedInput").value -1;
	//--solution
	var currentSolutionLine = String(solutionString[index]);
	//remove whitespace
	currentSolutionLine.split(" ", '');

	//remove #: 
	currentSolutionLine = currentSolutionLine.split(":")[1];

	//split instructions by robot
	var robotInstructions = currentSolutionLine.split(";");
	//console.log(robotInstructions);
	solutionRobots = [];
		
	var colourIndex = 0;
	console.log(robotInstructions);
	//instantiate new robots
	for (i=0; i< robotInstructions.length; i++) {
		solutionRobots.push(new robot(i));
		solutionRobots[i].add(solutionRobots[i], robotInstructions[i]);
		solutionRobots[i].colour = colourList[colourIndex%colourList.length];
		colourIndex++;
	}
	if(pathFlag){
		drawPaths(index)
	}	
	solutionRobots[0].state = true;
}


function drawPaths(num) {
	var pathCase = pathCases[num];
	// console.log(pathCase)
	
	var i, x, y;
	for (i=0; i<pathCase.length; i++) {
		if (!Array.isArray(pathCase[i])){
			pathCase[i] = pathCase[i].split(";");
		}
	}

	if (pathCase[0].length != 2) {
		pathCase.splice(0,1);
	}

	if (pathCase[pathCase.length - 1].length != 2) {
		pathCase.splice(pathCase.length-1, 1);
	}

	for (i=0; i<pathCase.length; i++) {
		for (y=0; y<pathCase[i].length; y++) {
			if (!Array.isArray(pathCase[i][y])){
				pathCase[i][y] = pathCase[i][y].split(",");
			}
		}
	}
	ctx.strokeStyle="#FF0000";

	for (i=0; i<pathCase.length; i++) {
		ctx.beginPath();
		//pathCase[path][to/from][x/y]
		ctx.moveTo((pathCase[i][0][0]*scaleY)+offsetX, (pathCase[i][0][1]*scaleY)+offsetY);
		ctx.lineTo((pathCase[i][1][0]*scaleY)+offsetX, (pathCase[i][1][1]*scaleY)+offsetY);
		ctx.stroke();
		ctx.closePath();
	}
	// console.log(pathCase);
}


function drawPrev() {
    document.getElementById("caseSelectedInput").value--;
    if(document.getElementById("caseSelectedInput").value<1) 
        document.getElementById("caseSelectedInput").value = 1;
    drawMap();
}

function drawNext() {
    document.getElementById("caseSelectedInput").value++;
    if( document.getElementById("caseSelectedInput").value > 30) 
        document.getElementById("caseSelectedInput").value = 30;
    drawMap();
}


function drawMap(){
    var index = document.getElementById("caseSelectedInput").value -1;

    var regExp = /\(([+-]?([0-9]*[.])?[0-9]+),([+-]?([0-9]*[.])?[0-9]+)\)/g; 
    calculateMaxMinPts(index);
    calculateScalingFactor();
    
    var ctx = document.getElementById('canvas').getContext('2d'); 
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    resizeCanvas();


    // After calling this function, robotString[] and obstacleString[] should then contain a string of Pts "(x1, y1), (x2, y2)..." for Q1-Q30
    splitInputString();
    //show(robotString);
    //show(obstacleString);

    // First draw robots
    var robots = robotString[index].match(regExp);
    for(var i=0; i<robots.length; i++){
        drawRobot(robots[i], i);
    }
    //show(robots);

    // Then draw obstacles 
    if(obstacleString[index].length>0){
        var obstacles = obstacleString[index].split(';');
        for(var i=0; i<obstacles.length; i++){
            drawObstacle(obstacles[i]);
        }
        //show(obstacles);                
    }

    // Lastly process the solution file
    calculateSolution();
}

function nextListener() {
	//when next button is clicked
	// var wake = [];
	console.log("next");
	var i;
	for (i=0; i<solutionRobots.length; i++) {
		console.log('sdf')
		solutionRobots[i].nextStep(solutionRobots[i]);
		solutionRobots[i].checkAwake(solutionRobots[i]);
		// wake.push(solutionRobots[i].state);
	}
}

/*function drawPath(){
    var ansString = ansTextbox.value;
    ansString = removeWhiteSpace(ansString)

    var paths = ansString.split(';');
    for(var i=0; i<paths.length; i++){
        drawLine(paths[i]);
    }
    //show(paths);
}


function drawLine(path){
    var regExp = /\(([+-]?([0-9]*[.])?[0-9]+),([+-]?([0-9]*[.])?[0-9]+)\)/g; 
    var ctx = canvas.getContext('2d'); 

    var points = path.match(regExp);
    var ptsX=[], ptsY=[];
    
    // Remove parenthesis
    for(var i=0; i<points.length; i++){
        points[i] = points[i].slice(1,-1);

        var split = points[i].split(',');
        ptsX.push(parseFloat(split[0]));
        ptsY.push(parseFloat(split[1]));
    }

    ctx.beginPath();
    ctx.moveTo((ptsX[0] - minPosX)*scalingFactor +10, (ptsY[0] - minPosY)*scalingFactor +10);
    for(var i=1; i<points.length; i++){
        ctx.lineTo((ptsX[i] - minPosX)*scalingFactor +10, (ptsY[i] - minPosY)*scalingFactor +10)
    }
    //ctx.strokeStyle =  "#"+((1<<24)*Math.random()|0).toString(16);
    ctx.strokeStyle =  'red';
    ctx.stroke();
    ctx.closePath();

    // Draw robot end-position
    var radius = 6;
    ctx.beginPath();
    ctx.arc((ptsX[points.length-1] - minPosX)*scalingFactor +10, (ptsY[points.length-1] - minPosY)*scalingFactor +10, radius, 0, 2 * Math.PI, false);

    ctx.lineWidth = 3;
    ctx.fillStyle = 'blue';
    ctx.fill();  
    ctx.closePath();
}
*/

function drawObstacle(obstacle){
    var regExp = /\(([+-]?([0-9]*[.])?[0-9]+),([+-]?([0-9]*[.])?[0-9]+)\)/g; 
    var ctx = canvas.getContext('2d'); 

    var points = obstacle.match(regExp);
    var ptsX=[], ptsY=[];

    // Remove parenthesis
    for(var i=0; i<points.length; i++){
        points[i] = points[i].slice(1,-1);

        var split = points[i].split(',');
        ptsX.push(parseFloat(split[0]));
        ptsY.push(parseFloat(split[1]));
    }

    ctx.beginPath();
    ctx.moveTo((ptsX[0] - minPosX)*scalingFactor +offSet, (ptsY[0] - minPosY)*scalingFactor +offSet);
    for(var i=1; i<points.length; i++){
        ctx.lineTo((ptsX[i] - minPosX)*scalingFactor +offSet, (ptsY[i] - minPosY)*scalingFactor+offSet)
    }
    ctx.fillStyle = '#4EEC91';
    ctx.fill();
    ctx.closePath();
}

function drawRobot(pts, i){
    var ctx = canvas.getContext('2d'); 
    // Remove parenthesis
    pts = pts.slice(1,-1);
    var split = pts.split(',');
    var ptsX = parseFloat(split[0]);
    var ptsY = parseFloat(split[1]);
    //show("point read - " + ptsX + ',' + ptsY);

    var radius = 5;
    ctx.beginPath();
    ctx.arc((ptsX - minPosX)*scalingFactor +offSet, (ptsY - minPosY)*scalingFactor +offSet, radius, 0, 2 * Math.PI, false);
    //show("point resized to - " + (ptsX - minPosX)*scalingFactor + ',' + (ptsY - minPosY)*scalingFactor);

    ctx.lineWidth = 3;
    ctx.strokeStyle = '#black';
    ctx.stroke();  
    ctx.closePath();

    // RAYMOND
	if (showRobotIndices) {
	    ctx.font = "11px Arial";
	    ctx.fillStyle="red";
	    ctx.fillText(i, (ptsX - minPosX)*scalingFactor +offSet + 6,  (ptsY - minPosY)*scalingFactor +offSet);
	}

}


function resizeCanvas(){
    var ctx = canvas.getContext('2d'); 
    ctx.canvas.height = window.innerHeight*0.9 + offSet*2;
    ctx.canvas.width = ctx.canvas.height + offSet*2;
}

// This should only be called after calculateMaxMinPts()
function calculateScalingFactor(){
    if(maxPosY-minPosY >= maxPosX-minPosX){
        scalingFactor = window.innerHeight*0.9/(maxPosY-minPosY);

    }else if(maxPosY-minPosY < maxPosX-minPosX){
        scalingFactor = window.innerHeight*0.9/(maxPosX-minPosX);
    }
    //show("scaling factor - " + scalingFactor);
}

function calculateMaxMinPts(index){
    var regExp = /\(([+-]?([0-9]*[.])?[0-9]+),([+-]?([0-9]*[.])?[0-9]+)\)/g; 
    var allPoints = inputString[index].match(regExp);
    //show(allPoints);

    maxPosX=0; minPosX=0; maxPosY=0; minPosY=0;
    for(var i=0; i<allPoints.length; i++){
        // Remove the parenthesis
        allPoints[i] = allPoints[i].slice(1,-1);
        var point = allPoints[i].split(',');

        var ptX = parseFloat(point[0]);
        var ptY = parseFloat(point[1]);

        // Find max and min values
        if(ptX > maxPosX) maxPosX = ptX;
        if(ptX < minPosX) minPosX = ptX;
        if(ptY > maxPosY) maxPosY = ptY;
        if(ptY < minPosY) minPosY = ptY;
    }

    //show("maximum pts - " + maxPosX + ',' + maxPosY);
    //show("minimum pts - " + minPosX + ',' + minPosY);
}





window.onload = function(){

	document.getElementById("fileInput").addEventListener("change", function(e){
	    var file = fileInput.files[0];
	    var textType = /text.*/;
	    if(file.type.match(textType)){

	        var reader = new FileReader();
	        reader.onload = function(e){
	            inputString = splitNewline(removeWhiteSpace(reader.result));
	            //console.log(inputString);
	        }
	        reader.readAsText(file);
	    } else{
	        alert("File not supported!");
	    }
	});

	document.getElementById("solutionInput").addEventListener("change", function(e){
	    var file = solutionInput.files[0];
	    var textType = /text.*/;
	    if(file.type.match(textType)){

	        var reader = new FileReader();
	        reader.onload = function(e){

	            solutionString = splitNewline(removeWhiteSpace(reader.result));
                solutionString.splice(0,2);
	        }
	        reader.readAsText(file);
	    } else{
	        alert("File not supported!");
	    }
	});

	document.getElementById("caseSelectedInput").addEventListener("input", function(e){
		if (document.getElementById("caseSelectedInput").value != "" ){
			caseSelected = document.getElementById("caseSelectedInput").value -1;
			// Every time the casenumber change, redraw the map
			// TODO:
			drawMap();
		}	
	});

	document.addEventListener("keydown", function(e) {
		if (e.keyCode == "190") {
			nextListener();
		}

		if (e.keyCode == "37") {
			drawPrev();
		}	
		if (e.keyCode == "39") {
			drawNext();
		}
		if (e.keyCode == "191") {
			drawMap();
		}

        // RAYMOND
        if (e.keyCode == "77") {
            showRobotIndices = !showRobotIndices;
            drawMap();
        }

	});
}

function splitInputString(){
    for(var i=0; i<inputString.length; i++){
        var splitted = inputString[i].split('#');
        robotString.push(splitted[0]);
        if(splitted.length>1){
            obstacleString.push(splitted[1]);
        }else{
            obstacleString.push("");
        }
    }
    //show(robots);
    //show(obstacles);
}

function removeWhiteSpace(string){
    string = string.replace(/ /g, '');
    return string;
}

function splitNewline(string){
    string = string.split("\n");
    return string;
}