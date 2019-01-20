// CALCULATOR.JS
//   Note: Look at 03_Sample program first
//
//

// 
class BinaryCalculator {
    
    constructor(elementId) {

	//number stored in memory
	this.mem = 0;

	//previous 
	this.prev = " + 0"

	//current text display;
	this.current = "0";

	//has an operator been used?
	this.operator = false;

	//the most recent result (number)
	this.result = 0;
	
        this.Model = {
        };
    
        this.View = {
            textRow : {id: "textRow", type: "text", value: "", onclick:""},
            button0 : {id: "button0", type: "button", value: 0, onclick:""},
            button1 : {id: "button1", type: "button", value: 1, onclick:""},
	    buttonAnd : {id: "buttonAnd", type: "button", value: "&", onclick:""},
	    buttonOr : {id: "buttonOr", type: "button", value: "|", onclick:""},
	    buttonNot : {id: "buttonNot", type: "button", value: "~", onclick:""},
	    buttonShiftLeft : {id: "buttonShiftLeft", type: "button", value: "<<", onclick:""},
	    buttonShiftRight : {id: "buttonShiftRight", type: "button", value: ">>", onclick:""},
	    buttonModulus : {id: "buttonModulus", type: "button", value: "%", onclick:""},
	    buttonClear : {id: "buttonClear", type: "button", value: "C", onclick:""},
	    buttonAdd : {id: "buttonAdd", type: "button", value: "+", onclick:""},
	    buttonMultiply : {id: "buttonMultiply", type: "button", value: "*", onclick:""},
	    buttonDivide : {id: "buttonDivide", type: "button", value: "/", onclick:""},
	    buttonEquals : {id: "buttonEquals", type: "button", value: "=", onclick:""},
	    buttonMR : {id: "buttonMR", type: "button", value: "MR", onclick:""},
	    buttonMC : {id: "buttonMC", type: "button", value: "MC", onclick:""},
	    buttonM_add : {id: "buttonM_add", type: "button", value: "M+", onclick:""},
            container : document.getElementById(elementId)
        };
    
        this.Controller = {
          viewClickHandler : function(e) {
             let target = e.target;
             if (target.id == "button0") {
		 this.button0Handler();
	     }else if (target.id == "button1") {
		 this.button1Handler();
             }else if (target.id == "buttonAdd") {
		 this.buttonAddHandler();
	     }else if (target.id == "buttonMultiply") {
		 this.buttonMultiplyHandler();
	     }else if (target.id == "buttonDivide") {
		 this.buttonDivideHandler();
	     }else if (target.id == "buttonEquals") {
		 this.buttonEqualsHandler();
	     }else if (target.id == "buttonClear") {
		 this.buttonClearHandler();
	     }else if (target.id == "buttonMR") {
		 this.buttonMRHandler();
	     }else if (target.id == "buttonM_add") {
		 this.buttonM_addHandler();
	     }else if (target.id == "buttonMC") {
		 this.buttonMCHandler();
	     }else if (target.id == "buttonAnd") {
		 this.buttonAndHandler();
	     }else if (target.id == "buttonOr") {
		 this.buttonOrHandler();
	     }else if (target.id == "buttonNot") {
		 this.buttonNotHandler();
	     }else if (target.id == "buttonShiftRight") {
		 this.buttonShiftRightHandler();
	     }else if (target.id == "buttonShiftLeft") {
		 this.buttonShiftLeftHandler();
	     }else if (target.id == "buttonModulus") {
		 this.buttonModulusHandler();
	     }
	      
          }
        }; 

	this.View.textRow.value = this.current;
	return this.renderView();
    
    } // end of constructor
    
    renderView(){
	this.attachButtonHandlers();
        let htmlString = this.createHTMLforView();
        console.log(htmlString);
        this.View.container.innerHTML = htmlString;
        return this;
    }
    
    //
    // attachButtonHandlers
    // determines what action is taken when a button is clicked
    // makes sure that when we click on a button or cell, the "this"
    // reference is fixed to that cell
    //
    attachButtonHandlers() {
       this.View.container.onclick 
          = this.Controller.viewClickHandler.bind(this);
    }


    //
    // button7Handler
    //
    button0Handler() {
      	this.current += "0";
	this.View.textRow.value = this.current;
	this.renderView();
    }

    button1Handler() {
        this.current += "1";
	this.View.textRow.value = this.current;
	this.renderView();
    }

    buttonAddHandler() {
	if(!this.operator){
	    this.current += " ";
      	    this.current += "+";
	    this.current += " ";
	    this.operator = true;
	    this.View.textRow.value = this.current;
	    this.renderView();
	} else {
	    alert("Duuuh, sorry, boss. That's too many operators for me to handle...");
	}     
    }

    buttonMultiplyHandler() {
	if(!this.operator){
	    this.current += " ";
      	    this.current += "*";
	    this.current += " ";
	    this.operator = true;
	    this.View.textRow.value = this.current;
	    this.renderView();
	} else {
	    alert("Duuuh, sorry, boss. That's too many operators for me to handle...");
	}      
    }

    buttonDivideHandler() {
	if(!this.operator){
	    this.current += " ";
      	    this.current += "/";
	    this.current += " ";
	    this.operator = true;
	    this.View.textRow.value = this.current;
	    this.renderView();
	} else {
	    alert("Duuuh, sorry, boss. That's too many operators for me to handle...");
	}      
    }

    buttonShiftRightHandler() {
	this.current = this.current.substring(0, this.current.length - 1);
	this.View.textRow.value = this.current;
	this.renderView();
    }

    buttonShiftLeftHandler() {
	this.current += "0";
	this.View.textRow.value = this.current;
	this.renderView();
    }

    buttonModulusHandler() {
	if(!this.operator){
	    this.current += " ";
      	    this.current += "%";
	    this.current += " ";
	    this.operator = true;
	    this.View.textRow.value = this.current;
	    this.renderView();
	} else {
	    alert("Duuuh, sorry, boss. That's too many operators for me to handle...");
	}	
    }

    buttonAndHandler() {
	if(!this.operator){
	    this.current += " ";
      	    this.current += "&";
	    this.current += " ";
	    this.operator = true;
	    this.View.textRow.value = this.current;
	    this.renderView();
	} else {
	    alert("Duuuh, sorry, boss. That's too many operators for me to handle...");
	}     
    }

    buttonOrHandler() {
	if(!this.operator){
	    this.current += " ";
      	    this.current += "|";
	    this.current += " ";
	    this.operator = true;
	    this.View.textRow.value = this.current;
	    this.renderView();
	} else {
	    alert("Duuuh, sorry, boss. That's too many operators for me to handle...");
	}     
    }

    buttonNotHandler() {
	if(!this.operator){
	    this.current += " ";
      	    this.current += "~";
	    this.current += " ";
	    this.operator = true;
	    this.View.textRow.value = this.current;
	    this.renderView();
	} else {
	    alert("Duuuh, sorry, boss. That's too many operators for me to handle...");
	}      
    }

    buttonClearHandler() {
	this.current = "0";
	this.operator = false;
	this.prev = " + 0";
	this.result = 0;
	this.View.textRow.value = this.current;
	this.renderView();     
    }

    buttonEqualsHandler() {
	var i;
	var length = this.current.length;

	var temp1, temp2, marker, calculation;
	var operator = null;
	
	for(i = 0; i < length; i++){
	    
	    if(this.current.charAt(i) == ' ' && operator == null){

		temp1 = parseInt(this.current.substring(0, i), 2);

		marker = i + 3;
		
		i++;
		operator = this.current.charAt(i);
		i++;
		
		if(operator == '~'){
		    temp1 = this.current.substring(0,i-2);
		    break;
		}
		
		if(marker >= length) return;
		
		continue;
	    }
	    
	    if(operator != null && i == length - 1){
		temp2 = parseInt(this.current.substring(marker), 2);
		break;
	    }
	}


	if(operator == '&' ||
	   operator == '|') {

	    temp1 = Number(temp1).toString(2);
	    temp2 = Number(temp2).toString(2);

	    var length1 = temp1.length;
	    var length2 = temp2.length;
	    var longerLength = length1 >= length2 ? length1 : length2;
	    temp1 = reverseString(temp1);
	    temp2 = reverseString(temp2);
	    for(i = 0; i < longerLength; i++){
		if(i > temp1.length - 1) temp1 += "0";
		if(i > temp2.length - 1) temp2 += "0";
	    }
	    temp1 = reverseString(temp1);
	    temp2 = reverseString(temp2);
	}
	
	switch(operator) {
	case '+':
	    calculation = temp1 + temp2;
	    break;
	case '-':
	    calculation = temp1 - temp2;
	    break;
	case '*':
	    calculation = temp1 * temp2;
	    break;
	case '/':
	    calculation = temp1 / temp2;
	    break;
	case '%':
	    calculation = temp1 % temp2;
	    break;
	case '&':
	    var string = "";
	    var newLength = temp1.length;
	    for(i = 0; i < newLength; i++){
		//start at the end of both strings
		//compare the values
		//if the same, add one to string
		//if not, add a zero
		//one out of bounds for either, add a zero
		if(temp2.length - 1 - i < 0 ||
		   temp1.length - 1 - i < 0) {

		    string += "0";
		    continue;
		}
		if(temp2.charAt(temp2.length - 1 - i) == "1" &&
		   temp1.charAt(temp1.length - 1 - i) == "1") {

		    string += "1";
		} else {
		    string += "0";
		}
	    }
	    string = reverseString(string);

	    this.prev = " " + operator + " " + this.current.substring(marker);
	    this.result = string;
	    this.current = this.result;
	    this.View.textRow.value = this.current;
	    this.operator = false;
	    this.renderView();
	    return;
	    break;
	case '|':
	    var string = "";
	    var newLength = temp1.length;
	    for(i = 0; i < newLength; i++){
		//start at the end of both strings
		//compare the values
		//if the same, add one to string
		//if not, add a zero
		//one out of bounds for either, add a zero
		if(temp2.length - 1 - i < 0 ||
		   temp1.length - 1 - i < 0) {
		    alert(temp2.length);
		    alert(temp1.length);
		    alert("NOOO");
		    string += "0";
		    continue;
		}
		if(temp2.charAt(temp2.length - 1 - i) == "1" ||
		   temp1.charAt(temp1.length - 1 - i) == "1") {

		    string += "1";
		} else {
		    string += "0";
		}
	    }
	    string = reverseString(string);

	    this.prev = " " + operator + " " + this.current.substring(marker);
	    this.result = string;
	    this.current = this.result;
	    this.View.textRow.value = this.current;
	    this.operator = false;
	    this.renderView();
	    return;
	    break;
	case '~':
	    var string = "";
	    for(i = 0; i < temp1.length; i++){
		if(temp1.charAt(i) == "0") string += "1";
		if(temp1.charAt(i) == "1") string += "0";
	    }	
	    this.prev = " " + operator + " ";
	    this.result = string;
	    this.current = this.result;
	    this.View.textRow.value = this.current;
	    this.operator = false;
	    this.renderView();
	    return;
	case null:
	    this.current += this.prev;
	    this.buttonEqualsHandler();
	    return;
	}
	this.prev = " " + operator + " " + this.current.substring(marker);
	this.result = parseInt(Math.round(calculation), 10).toString(2);
	this.current = this.result;
	this.View.textRow.value = this.current;
	this.operator = false;
	this.renderView();
    }
    
    buttonMRHandler() {
	this.current += this.mem.toString();
	this.View.textRow.value = this.current;
	this.renderView();      
    }

    buttonM_addHandler() {
	var temp = "";
	temp = this.prev;
	this.prev = " + 0";
	
	this.buttonEqualsHandler();

	this.prev = temp;
	
	this.mem += this.result;      
    }

    buttonMCHandler() {
	this.mem = 0;     
    }
    
    //
    // createHTMLforView
    // Utility. creates HTML formatted text for the entire view
    //
    createHTMLforView() {
      var s;
      s = "<table id=\"myTable\" border=2>"

      // row for results
      s += "<tr><td>" + this.createHTMLforElement(this.View.textRow) + "</td></tr>";

	// thisulator buttons
	s +="<tr><td>";
	s += this.createHTMLforElement(this.View.button0);
	s += this.createHTMLforElement(this.View.button1);
	s +="</td></tr>";
	
	s +="<tr><td>";
	s += this.createHTMLforElement(this.View.buttonAdd);
	s += this.createHTMLforElement(this.View.buttonMultiply);
	s += this.createHTMLforElement(this.View.buttonDivide);
	s +="</td></tr>";

	s +="<tr><td>";
	s += this.createHTMLforElement(this.View.buttonShiftRight);
	s += this.createHTMLforElement(this.View.buttonShiftLeft);
	s += this.createHTMLforElement(this.View.buttonModulus);
	s +="</td></tr>";

	s +="<tr><td>";
	s += this.createHTMLforElement(this.View.buttonAnd);
	s += this.createHTMLforElement(this.View.buttonOr);
	s += this.createHTMLforElement(this.View.buttonNot);
	s +="</td></tr>";

	s +="<tr><td>";
	s += this.createHTMLforElement(this.View.buttonClear);
	s += this.createHTMLforElement(this.View.buttonEquals);
	s +="</td></tr>";
	
	s +="<tr><td>";
	s += this.createHTMLforElement(this.View.buttonMR);
	s += this.createHTMLforElement(this.View.buttonM_add);
	s += this.createHTMLforElement(this.View.buttonMC);
	s +="</td></tr>";
	
      s += "</tr></td></table>";
      return s;
    }


    //
    // createHTMLforElement
    // utility. creates html formatted text for an element
    //
    createHTMLforElement(element) {
      var s = "<input ";
      s += " id=\"" + element.id + "\"";
      s += " type=\"" + element.type + "\"";
      s += " value= \"" + element.value + "\"";
      s += " onclick= \"" + element.onclick + "\"";
      s += ">";
      return s;
    }

} // end of Calculator;

//https://medium.freecodecamp.org/how-to-reverse-a-string-in-javascript-in-3-different-ways-75e4763c68cb
function reverseString(str) {
    // Step 1. Create an empty string that will host the new created string
    var newString = "";
 
    // Step 2. Create the FOR loop
    /* The starting point of the loop will be (str.length - 1) which corresponds to the 
       last character of the string, "o"
       As long as i is greater than or equals 0, the loop will go on
       We decrement i after each iteration */
    for (var i = str.length - 1; i >= 0; i--) { 
        newString += str[i]; // or newString = newString + str[i];
    }
    /* Here hello's length equals 5
        For each iteration: i = str.length - 1 and newString = newString + str[i]
        First iteration:    i = 5 - 1 = 4,         newString = "" + "o" = "o"
        Second iteration:   i = 4 - 1 = 3,         newString = "o" + "l" = "ol"
        Third iteration:    i = 3 - 1 = 2,         newString = "ol" + "l" = "oll"
        Fourth iteration:   i = 2 - 1 = 1,         newString = "oll" + "e" = "olle"
        Fifth iteration:    i = 1 - 1 = 0,         newString = "olle" + "h" = "olleh"
    End of the FOR Loop*/
 
    // Step 3. Return the reversed string
    return newString; // "olleh"
}
