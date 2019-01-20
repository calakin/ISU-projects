// CALCULATOR.JS
//   Note: Look at 03_Sample program first
//
//
// 
class Calculator {
    
    constructor(elementId) {

	//number stored in memory
	this.mem = 0;

	//previous 
	this.prev = " + 0"

	//current text display;
	this.current = "0";

	//has an operator been used?
	this.operator = false;

	//has dot been used in current value?
	this.dot = false;

	//the most recent result (number)
	this.result = 0;
	
        this.Model = {
        };
    
        this.View = {
            textRow : {id: "textRow", type: "text", value: "", onclick:""},
	    button0 : {id: "button0", type: "button", value: 0, onclick:""},
	    button1 : {id: "button1", type: "button", value: 1, onclick:""},
	    button2 : {id: "button2", type: "button", value: 2, onclick:""},
	    button3 : {id: "button3", type: "button", value: 3, onclick:""},
	    button4 : {id: "button4", type: "button", value: 4, onclick:""},
	    button5 : {id: "button5", type: "button", value: 5, onclick:""},
	    button6 : {id: "button6", type: "button", value: 6, onclick:""},
            button7 : {id: "button7", type: "button", value: 7, onclick:""},
            button8 : {id: "button8", type: "button", value: 8, onclick:""},
	    button9 : {id: "button9", type: "button", value: 9, onclick:""},
	    buttonDot : {id: "buttonDot", type: "button", value: ".", onclick:""},
	    buttonClear : {id: "buttonClear", type: "button", value: "C", onclick:""},
	    buttonSubtract : {id: "buttonSubtract", type: "button", value: "-", onclick:""},
	    buttonAdd : {id: "buttonAdd", type: "button", value: "+", onclick:""},
	    buttonMultiply : {id: "buttonMultiply", type: "button", value: "*", onclick:""},
	    buttonDivide : {id: "buttonDivide", type: "button", value: "/", onclick:""},
	    buttonEquals : {id: "buttonEquals", type: "button", value: "=", onclick:""},
	    buttonMR : {id: "buttonMR", type: "button", value: "MR", onclick:""},
	    buttonMC : {id: "buttonMC", type: "button", value: "MC", onclick:""},
	    buttonM_add : {id: "buttonM_add", type: "button", value: "M+", onclick:""},
	    buttonM_subtract : {id: "buttonM_subtract", type: "button", value: "M-", onclick:""},
            container : document.getElementById(elementId)
        };
    
        this.Controller = {
          viewClickHandler : function(e) {
             let target = e.target;
             if (target.id == "button0") {
               this.button0Handler();
             } else if (target.id == "button1") {
		 this.button1Handler();
	     }else if (target.id == "button2") {
		 this.button2Handler();
	     }else if (target.id == "button3") {
		 this.button3Handler();
	     }else if (target.id == "button4") {
		 this.button4Handler();
	     }else if (target.id == "button5") {
		 this.button5Handler();
	     }else if (target.id == "button6") {
		 this.button6Handler();
	     }else if (target.id == "button7") {
		 this.button7Handler();
	     }else if (target.id == "button8") {
		 this.button8Handler();
	     }else if (target.id == "button9") {
		 this.button9Handler();
	     }else if (target.id == "buttonAdd") {
		 this.buttonAddHandler();
	     }else if (target.id == "buttonSubtract") {
		 this.buttonSubtractHandler();
	     }else if (target.id == "buttonMultiply") {
		 this.buttonMultiplyHandler();
	     }else if (target.id == "buttonDivide") {
		 this.buttonDivideHandler();
	     }else if (target.id == "buttonDot") {
		 this.buttonDotHandler();
	     }else if (target.id == "buttonEquals") {
		 this.buttonEqualsHandler();
	     }else if (target.id == "buttonClear") {
		 this.buttonClearHandler();
	     }else if (target.id == "buttonMR") {
		 this.buttonMRHandler();
	     }else if (target.id == "buttonM_subtract") {
		 this.buttonM_subtractHandler();
	     }else if (target.id == "buttonM_add") {
		 this.buttonM_addHandler();
	     }else if (target.id == "buttonMC") {
		 this.buttonMCHandler();
	     } 
          }
        };
	this.View.textRow.value = this.current;
	this.renderView();
    } // end of constructor
    
    //update the view
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

    button2Handler() {
      	this.current += "2";
	this.View.textRow.value = this.current;
	this.renderView()
    }

    button3Handler() {
      	this.current += "3";
	this.View.textRow.value = this.current;
	this.renderView()
    }

    button4Handler() {
      	this.current += "4";
	this.View.textRow.value = this.current;
	this.renderView();
    }

    button5Handler() {
      	this.current += "5";
	this.View.textRow.value = this.current;
	this.renderView();
    }

    button6Handler() {
      	this.current += "6";
	this.View.textRow.value = this.current;
	this.renderView();
    }

    button7Handler() {
      	this.current += "7";
	this.View.textRow.value = this.current;
	this.renderView();
    }

    button8Handler() {
      	this.current += "8";
	this.View.textRow.value = this.current;
	this.renderView();
    }

    button9Handler() {
      	this.current += "9";
	this.View.textRow.value = this.current;
	this.renderView();
    }
    
    buttonDotHandler() {
	if(this.dot){
	    alert("Only one dot per number!");
	    return;
	}
      	this.current += ".";
	this.dot = true;
	this.View.textRow.value = this.current;
	this.renderView();
    }

    buttonAddHandler() {
	if(!this.operator){
	    this.current += " ";
      	    this.current += "+";
	    this.current += " ";
	    this.operator = true;
	    this.dot = false;
	    this.View.textRow.value = this.current;
	    this.renderView();
	} else {
	    alert("Duuuh, sorry, boss. That's too many operators for me to handle...");
	}
    }

    buttonSubtractHandler() {
	if(!this.operator){
	    this.current += " ";
      	    this.current += "-";
	    this.current += " ";
	    this.operator = true;
	    this.dot = false;
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
	    this.dot = false;
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
	    this.dot = false;
	    this.renderView();
	} else {
	    alert("Duuuh, sorry, boss. That's too many operators for me to handle...");
	}
    }
    
    buttonClearHandler() {
	this.current = "0";
	this.operator = false;
	this.dot = false;
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
	var float = false;
	
	for(i = 0; i < length; i++){
	    if(this.current.charAt(i) == '.') float = true;
	    
	    if(this.current.charAt(i) == ' ' && operator == null){
		if(float){
		    temp1 = parseFloat(this.current.substring(0,i));
		    float = false;
		} else {
		    temp1 = parseInt(this.current.substring(0, i));
		}
		marker = i + 3;

		if(marker >= length) return;
		
		i++;
		operator = this.current.charAt(i);
		i++;
		continue;
	    }
	    
	    if(operator != null && i == length - 1){
		if(float) {
		    temp2 = parseFloat(this.current.substring(marker));
		    float = false;
		} else{
		    temp2 = parseInt(this.current.substring(marker));
		}
		break;
	    }
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
	case null:
	    this.current += this.prev;
	    this.buttonEqualsHandler();
	    return;
	}
	this.prev = " " + operator + " " + this.current.substring(marker);
	this.result = calculation;
	this.current = this.result.toString();
	this.View.textRow.value = this.current;
	this.operator = false;
	this.dot = false;
	this.renderView();
    }

    buttonMRHandler() {
	this.current += this.mem.toString();
	this.View.textRow.value = this.current;
	this.renderView();
    }

    buttonM_subtractHandler() {
	var temp = "";
	temp = this.prev;
	this.prev = " + 0";
	
	this.buttonEqualsHandler()

	this.prev = temp;
	
	this.mem -= this.result;
    }

    buttonM_addHandler() {
	var temp = "";
	temp = this.prev;
	this.prev = " + 0";
	
	this.buttonEqualsHandler()

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
	s += this.createHTMLforElement(this.View.button7);
	s += this.createHTMLforElement(this.View.button8);
	s += this.createHTMLforElement(this.View.button9);
	s += this.createHTMLforElement(this.View.buttonAdd);
	s +="</td></tr>";

	s +="<tr><td>";
	s += this.createHTMLforElement(this.View.button4);
	s += this.createHTMLforElement(this.View.button5);
	s += this.createHTMLforElement(this.View.button6);
	s += this.createHTMLforElement(this.View.buttonSubtract);
	s +="</td></tr>";

	s +="<tr><td>";
	s += this.createHTMLforElement(this.View.button1);
	s += this.createHTMLforElement(this.View.button2);
	s += this.createHTMLforElement(this.View.button3);
	s += this.createHTMLforElement(this.View.buttonMultiply);
	s +="</td></tr>";

	s +="<tr><td>";
	s += this.createHTMLforElement(this.View.button0);
	s += this.createHTMLforElement(this.View.buttonDot);
	s += this.createHTMLforElement(this.View.buttonEquals);
	s += this.createHTMLforElement(this.View.buttonDivide);
	s +="</td></tr>";

	s +="<tr><td>";
	s += this.createHTMLforElement(this.View.buttonClear);
	s +="</td></tr>";


	s +="<tr><td>";
	s += this.createHTMLforElement(this.View.buttonMR);
	s += this.createHTMLforElement(this.View.buttonM_subtract);
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

