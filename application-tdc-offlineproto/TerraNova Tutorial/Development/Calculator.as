import mx.controls.Alert;
class Calculator extends MovieClip {
	private var isdec = false;
	//this tells if the curret input has had a decimal put in it
	private var calculation:String = "";
	//this will hold the expression to be evaulated
	private var isstart = true;
	//tells that a new input has to be accepted in
	private var isFraction:Boolean = false;
	////////////////////////////////////////
	private var operatorTxt:TextField;
	private var memoryTxt:TextField;
	private var displayTxt:TextField;
	private var numeTxt:TextField;
	private var denoTxt:TextField;
	private var dashLineTxt:TextField;
	//////////////////////////////////////
	private var operator:String = "";
	private var mem:String = "";
	private var value:String = "";
	private var nume:String = "";
	private var temp_nume:String = "";
	private var deno:String = "";
	private var dashLine:String = "___";
	private var mc:MovieClip;
	private var stack:Array;
	private var tempStack:Array;
	private var clear:Boolean = false;
	////////////////////////////////////////
	private var focusNumeratorText:Boolean = false;
	private var focusDenominatorText:Boolean = false;
	private var focusDenominatorText_proper:Boolean = false;
	//
	private var bracketsOpened:Boolean = false;
	private var bracketsClosed:Boolean = false;
	//
	private var last_keyPressed_is_operator:Boolean = false;
	private var last_keyPressed_is_equal:Boolean = false;
	private var last_keyPressed_is_PR:Boolean = false;
	private var CF_toggle:Boolean = false;
	private var FD_toggle:Boolean = false;
	private var isMemoryOperation:Boolean = false;
	private var MPlus_clicked:Boolean = false;
	private var MMinus_clicked:Boolean = false;
	/////////////////////////////////////
	private var fractValue:Number = 0;
	private var NumeratorValue:Number = 0;
	private var DenominatorValue:Number = 0;
	private var Numerator:Number = 0;
	private var Denominator:Number = 0;
	//
	private var Memory_value:Number = 0;
	private var valueHolder:String = "";
	private var numToBeDivided:String = "";
	private var decValue:Number = 0;
	private var ansValue:Number = 0;
	/////////////////////////////////////
	private var stack_secondLastElement:String = "";
	private var stack_lastElement:String = "";
	/////////////////////////////////////
	function Calculator() {
		//
		mc = this;
		trace("mc"+mc);
		//
		_parent.minimizedState_mc._visible = false;
		//
		stack = new Array();
		tempStack = new Array();
		addHandler();
		AllClear();
		//
		displayTxt.type = "input";
		operatorTxt.type = "input";
		numeTxt.type = "input";
		denoTxt.type = "input";
		//
		displayTxt.maxChars = 10;
		operatorTxt.maxChars = 4;
		numeTxt.maxChars = 4;
		denoTxt.maxChars = 4;
		//
	}
	//
	private function addHandler() {
		var thisRef = this;
		////////////// digit press ////////////////////////////
		mc.d9.onRelease = function() {
			//thisRef.Append(9);
			thisRef.onReleases("9", "Append");
		};
		mc.d8.onRelease = function() {
			//thisRef.Append(8);
			thisRef.onReleases("8", "Append");
		};
		mc.d7.onRelease = function() {
			//thisRef.Append(7);
			thisRef.onReleases("7", "Append");
		};
		mc.d6.onRelease = function() {
			//thisRef.Append(6);
			thisRef.onReleases("6", "Append");
		};
		mc.d5.onRelease = function() {
			//thisRef.Append(5);
			thisRef.onReleases("5", "Append");
		};
		mc.d4.onRelease = function() {
			//thisRef.Append(4);
			thisRef.onReleases("4", "Append");
		};
		mc.d3.onRelease = function() {
			//thisRef.Append(3);
			thisRef.onReleases("3", "Append");
		};
		mc.d2.onRelease = function() {
			//thisRef.Append(2);
			thisRef.onReleases("2", "Append");
		};
		mc.d1.onRelease = function() {
			//thisRef.Append(1);
			thisRef.onReleases("1", "Append");
		};
		mc.d0.onRelease = function() {
			//thisRef.Append(0);
			thisRef.onReleases("0", "Append");
		};
		mc.dPoint.onRelease = function() {
			thisRef.onReleases(".", "Append");
			//thisRef.Append(".");
		};
		mc.dPI.onRelease = function() {
			thisRef.onReleases("PI", "Append");
			//thisRef.Append(Math.PI);
		};
		////////////// End digit press ////////////////////////////
		/////////////// Operation ////////////////////////////////
		mc.opPlus.onRelease = function() {
			thisRef.onReleases("+", "StandOp");
			//thisRef.StandOp("+");
		};
		mc.opMulti.onRelease = function() {
			thisRef.onReleases("*", "StandOp");
			//thisRef.StandOp("*");
		};
		mc.opSign.onRelease = function() {
			thisRef.onReleases("+/-", "changeSign");
			//thisRef.StandOp("-/+");
		};
		mc.opEqual.onRelease = function() {
			thisRef.onReleases("=", "StandOp");
			//thisRef.StandOp("=");
		};
		mc.opMinus.onRelease = function() {
			thisRef.onReleases("-", "StandOp");
			//thisRef.StandOp("-");
		};
		mc.opDiv.onRelease = function() {
			thisRef.onReleases("/", "StandOp");
			//thisRef.StandOp("/");
		};
		/////////////// End Operation ////////////////////////////////
		///////////// Special Operations ////////////////////////////
		mc.opRemind.onRelease = function() {
			thisRef.onReleases("%R", "SplOp");
			//thisRef.StandOp("%R");
		};
		mc.percent.onRelease = function() {
			//thisRef.StandOp("%");
			thisRef.onReleases("%", "SplOp");
		};
		mc.sqrt.onRelease = function() {
			//thisRef.StandOp("%");
			thisRef.onReleases("sqrt", "SplOp");
		};
		mc.openBrac.onRelease = function() {
			//thisRef.StandOp("(");
			thisRef.onReleases("(", "SplOp");
		};
		mc.closeBrac.onRelease = function() {
			//thisRef.StandOp(")");
			thisRef.onReleases(")", "SplOp");
		};
		//----------------------------------------------------------
		mc.ATbc.onRelease = function() {
			thisRef.onReleases("a", "SplOp");
		};
		mc.aTBC.onRelease = function() {
			thisRef.onReleases("b/c", "SplOp");
		};
		mc.abcTOdc.onRelease = function() {
			thisRef.onReleases("CF_SF", "SplOp");
		};
		mc.fTd.onRelease = function() {
			thisRef.onReleases("F_D", "SplOp");
		};
		mc.Simp.onRelease = function() {
			thisRef.onReleases("simplify", "SplOp");
		};
		//////////// End Of Special Operations ///////////////////////
		/////////////// Advance Operation ///////////////////////////
		mc.Mminus.onRelease = function() {
			//thisRef.StandOp("M-");
			thisRef.onReleases("M-", "Mem");
		};
		mc.Mplus.onRelease = function() {
			//thisRef.StandOp("M+");
			thisRef.onReleases("M+", "Mem");
		};
		mc.Mr.onRelease = function() {
			//thisRef.StandOp("MR");
			thisRef.onReleases("MR", "Mem");
		};
		mc.Mc.onRelease = function() {
			//thisRef.StandOp("Mc");
			thisRef.onReleases("Mc", "Mem");
		};
		mc.Help.onRelease = function() {
			//thisRef.StandOp("Help");
			//thisRef.onReleases("%R","StandOp");
		};
		mc.clear.onRelease = function() {
			//thisRef.StandOp("C");
			thisRef.onReleases("C", "Clear");
		};
		mc.AC.onRelease = function() {
			//thisRef.StandOp("AC");
			thisRef.onReleases("AC", "AllClear");
		};
		////////////////////////////////////////////////////////////
	}
	//*************************************************************
	private function onRollOvers(clip:MovieClip) {
		clip.gotoAndStop(2);
	}
	/////////////////////////////////////////////////////
	private function onRollOuts(clip:MovieClip) {
		clip.gotoAndStop(1);
	}
	////////////////////////////////////////////////////////
	private function onReleases(_str:String, opStr:String) {
		trace("launch");
		switch (opStr) {
		case "Append" :
			this.Append(_str);
			break;
		case "StandOp" :
			this.StandOp(_str);
			break;
		case "Mem" :
			this.Mem(_str);
			break;
		case "Clear" :
			this.Clear();
			break;
		case "AllClear" :
			this.AllClear();
			break;
		case "changeSign" :
			changeSign(_str);
			break;
		case "SplOp" :
			this.SpecialOperations(_str);
			break;
		}
	}
	//////////////////////////////////////////////
	private function Append(_strs:String) {
		//
		trace("\nWhen NUMBER is Pressed !!");
		trace("this.operator ===== "+this.operator);
		//
		if (this.operator == "=") {
			this.operator = "";
		}
		if (_strs == "PI") {
			_strs = (Math.PI).toString();
		}
		if (this.clear == true){
			//trace("______Inside Append IF_____")
			trace("_strs ===== "+_strs);
			trace("*** this.operator == "+this.operator);
			trace("this.stack[this.stack.length-1]  == "+this.stack[this.stack.length-1]);
			//
			if (((focusDenominatorText) && (_strs == "0")) || ((this.stack[this.stack.length-1] == "/") && (_strs == "0"))) {
				//|| ((this.stack[this.stack.length-1] == "/") && (_strs == "0"))) {
				Alert.show("The denominator must be greater than 0.", "Alert");
				this.value = "";
				//fractValue = NumeratorValue;
			} else {
				this.value = _strs;
				this.clear = false;
			}
		} else {
			//trace("______Inside Append ELSE_____")
			if ((isFraction) && (focusDenominatorText)) {
				if (denoTxt.length<4) {
					this.value += _strs;
				} else {
					Alert.show("The number is too large for the display", "Alert");
				}
			} else if ((isFraction) && (focusNumeratorText)) {
				if (numeTxt.length<4) {
					this.value += _strs;
				} else {
					//("The Number is too large for display");
				}
			} else if (displayTxt.length<10) {
				this.value += _strs;
			} else {
				//Alert.show("The number is too large for the display","Alert");
			}
			//   
		}
		//////////////////////////////////////
		if (!isFraction) {
			//
			display(true, this.value);
			valueHolder = this.value;
			fractValue = Number(this.value);
			// 
		} else {
			if (focusDenominatorText) {
				trace("\nInside focusDenominatorText >>>>>");
				//
				if (!focusNumeratorText) {
					trace("-----------When NOT focusNumeratorText----------");
					trace("fractValue ~~~~ "+fractValue);
					trace("valueHolder ~~~~ "+valueHolder);
					trace(" denoTxt.text.length == "+denoTxt.text.length);
					//
					if (denoTxt.text.length == 0) {
						nume = String(fractValue);
						temp_nume = nume;
					} else {
						nume = temp_nume;
					}
					dashLine = "___";
					deno = this.value;
					//
					display(true, "");
					//
					NumeratorValue = Number(nume);
					DenominatorValue = Number(deno);
					//
					trace(":::: Before :::: fractValue =="+fractValue);
					trace("&&& DenominatorValue $$$ "+DenominatorValue);
					if ((DenominatorValue != "0") && (!isNaN(Number(DenominatorValue)))) {
						trace("Inside DenominatorValue");
						fractValue = 0;
					}
					trace(":::: After :::: fractValue =="+fractValue);
					//      
				} else if (focusDenominatorText_proper) {
					trace("******** When focusDenominatorText_proper **********");
					trace("fractValue ~~~~ "+fractValue);
					trace("valueHolder ~~~~ "+valueHolder);
					//
					dashLine = "___";
					deno = this.value;
					//
					display(true, String(fractValue));
					//
					NumeratorValue = Number(nume);
					DenominatorValue = Number(deno);
				} else if (focusNumeratorText) {
					trace("~~~~~~~~~ When focusNumeratorText ~~~~~~~~~");
					trace("fractValue ~~~~ "+fractValue);
					trace("valueHolder ~~~~ "+valueHolder);
					//
					nume = this.value;
					dashLine = "___";
					deno = "";
					//
					display(true, String(fractValue));
					//
					NumeratorValue = Number(nume);
					DenominatorValue = 1;
					//~~~~~~~~
					trace("## nume = "+nume);
					trace("## deno = "+deno);
				}
			} else if (focusNumeratorText) {
				trace("\nInside focusNumeratorText *************** ");
				trace("fractValue ~~~~ "+fractValue);
				trace("valueHolder ~~~~ "+valueHolder);
				//
				nume = this.value;
				dashLine = "___";
				deno = "";
				//
				display(true, String(fractValue));
				//
				NumeratorValue = Number(nume);
				DenominatorValue = 1;
				//~~~~~~~~~~
				focusDenominatorText_proper = true;
			}
			trace("^^^^ After Loops ^^^^");
			trace("fractValue = "+fractValue);
			trace("NumeratorValue = "+NumeratorValue);
			trace("DenominatorValue = "+DenominatorValue);
			trace("^^^^^^^^^^^^^^^^^^^^^^^^");
			//
			Numerator = WND_To_ND(fractValue, NumeratorValue, DenominatorValue).numerator;
			Denominator = WND_To_ND(fractValue, NumeratorValue, DenominatorValue).denominator;
			//
			valueHolder = fraction_To_decimal(Numerator, Denominator).decimal;
			//
			trace("valueHolder ------>>>> "+valueHolder);
		}
		//
		last_keyPressed_is_operator = false;
		last_keyPressed_is_equal = false;
		//
		trace("------ Out From Append ------");
		trace(" valueHolder === "+valueHolder);
		trace("-----------------------------");
	}
	///////////////////////////////////////////
	private function changeSign(op:String) {
		trace("Inside changeSign == "+valueHolder);
		if (op == "+/-") {
			this.value = (-(Number(valueHolder))).toString();
			this.operator = "";
		}
		//                                                                                                                                               
		display(true, this.value);
		valueHolder = this.value;
		//
		//last_keyPressed_is_operator = true;
		//
	}
	/////////////////////////////////////////////
	private function StandOp(op:String) {
		//
		nume = "";
		dashLine = "";
		deno = "";
		//
		focusDenominatorText_proper = undefined;
		focusNumeratorText = undefined;
		focusDenominatorText = undefined;
		FD_toggle = undefined;
		CF_toggle = undefined;
		MPlus_clicked = true;
		MMinus_clicked = true;
		if (op == "=") {
			trace("\nCurrently operator is '=' ");
			if (last_keyPressed_is_PR) {
				//
				calculationBeforeOperatorPressed();
				//
				trace("When the operation is '%R' ");
				trace("Num To be Devided == "+numToBeDivided);
				trace("Divisor Num == "+Number(this.value));
				//
				var division = Math.floor(Number(numToBeDivided)/Number(this.value));
				var remainder = Math.abs(Number(numToBeDivided)%Number(this.value));
				var dispValue = division+" R "+remainder;
				//
				this.ansValue = (Number(numToBeDivided)/Number(this.value));
				//
				this.stack.length = 0;
				this.stack.push(this.ansValue);
				//
				display(true, dispValue.toString());
				valueHolder = String(this.ansValue);
				//
				numToBeDivided = "";
				//
				last_keyPressed_is_operator = true;
				last_keyPressed_is_PR = false;
				//
			} else if ((bracketsOpened != true) && (bracketsClosed != true)) {
				if (last_keyPressed_is_equal) {
					trace("Last KeyPressed Is Equal !!");
					//
					this.stack.length = 0;
					this.stack.push(valueHolder);
					this.stack.push(stack_secondLastElement);
					this.stack.push(stack_lastElement);
					//
					var newValue = "";
					for (var i = 0; i<this.stack.length; i++) {
						newValue += this.stack[i];
					}
					trace("newValue == "+newValue);
					// 
					this.ansValue = _parent.evals(newValue);
					//
					trace("== valueHolder == "+String(this.ansValue));
					//
					if (String(this.ansValue).length<=8) {
						display(true, String(this.ansValue));
						valueHolder = String(this.ansValue);
					} else {
						Alert.show("The number is too large for the display", "Alert");
						AllClear();
					}
					//
				} else {
					//
					calculationBeforeOperatorPressed();
					//
					trace("Last KeyPressed Is NOT Equal !!");
					//
					if ((valueHolder.length<=8) && ((valueHolder != "Infinity") || (valueHolder != "-Infinity"))) {
						//					
						display(true, valueHolder);
						//
						this.stack.length = 0;
						this.stack.push(valueHolder);
					} else {
						Alert.show("The number is too large for the display", "Alert");
						AllClear();
					}
				}
				//
				last_keyPressed_is_operator = true;
				last_keyPressed_is_equal = true;
				//
				trace(" Stack === "+this.stack);
				trace("Displayed Value === "+valueHolder);
				trace(" valueHolder  === "+valueHolder);
			}
			//                
			bracketsOpened = false;
			bracketsClosed = false;
			//                             
			trace("valueHolder : "+valueHolder);
		} else {
			trace("\nCurrently operator is NOT '=' ");
			trace("When OPERATOR is Pressed !!");
			//
			this.operator = op;
			//
			if (!last_keyPressed_is_operator) {
				trace("When Last Key Pressed is NOT an Operator !!");
				trace("valueHolder ======== "+valueHolder);
				if (((last_keyPressed_is_equal) || (bracketsClosed)) && (valueHolder != "Infinity")) {
					this.stack.length = 0;
					this.stack.push(valueHolder);
				} else {
					//
					calculationBeforeOperatorPressed();
					//
				}
				this.stack.push(this.operator);
			} else {
				trace("When Last Key Pressed is an Operator !!");
				if (last_keyPressed_is_equal) {
					this.stack[0] = valueHolder;
				}
				var index = this.stack.length-1;
				trace("Get The Last Array Element : Before === "+this.stack[index]);
				this.stack.splice(1, index);
				trace("Get The Last Array Element : After === "+this.stack[index]);
				this.stack.push(this.operator);
			}
			this.decValue = Number(this.value);
			//
			display(true, valueHolder);
			//valueHolder = this.value;
			//
			this.operator = "";
			last_keyPressed_is_operator = true;
			last_keyPressed_is_equal = false;
			//
			trace(" Stack >>> "+this.stack);
			trace("Displayed Value >>> "+valueHolder);
			trace(" valueHolder  >>> "+valueHolder);
		}
		//
		isFraction = false;
		this.clear = true;
		//
	}
	////////////////////////////////////////////////////////////
	private function Mem(_strs:String) {
		//
		if (_strs == "M+") {
			//
			isMemoryOperation = true;
			if (!MPlus_clicked) {
				//
				calculationBeforeOperatorPressed();
				//
			}
			trace("$$$ valueHolder ~~~~ "+valueHolder);
			Memory_value += Number(valueHolder);
			this.mem = "M";
			display(true, valueHolder);
			//
			//valueHolder = "0";
			this.value = "";
			this.operator = "";
			this.stack.length = 0;
			MPlus_clicked = true;
			//
		} else if (_strs == "M-") {
			//
			isMemoryOperation = true;
			if (!MMinus_clicked) {
				//
				calculationBeforeOperatorPressed();
				//
			}
			trace("$$$ valueHolder ~~~~ "+valueHolder);
			Memory_value -= Number(valueHolder);
			this.mem = "M";
			display(true, valueHolder);
			//
			//valueHolder = "0";
			this.value = "";
			this.operator = "";
			this.stack.length = 0;
			MMinus_clicked = true;
			//
		} else if (_strs == "MR") {
			//
			//isMemoryOperation = false;
			//
			nume = "";
			dashLine = "";
			deno = "";
			this.value = String(Memory_value);
			display(true, this.value);
			valueHolder = this.value;
			//
		} else if (_strs == "Mc") {
			//
			Memory_value = 0;
			this.mem = "";
			display(true, valueHolder);
			isMemoryOperation = false;
			//
		}
		//                                                                                                                                         
		isstart = true;
		isdec = false;
	}
	////////////////////////////////////////////////////////////////
	/*private function displayClear() {
	this.displayTxt.text = "";
	this.clear = true;
	}*/
	//////////////////////////////////////////////////////////////////
	private function Clear() {
		//
		focusNumeratorText = undefined;
		focusDenominatorText = undefined;
		focusDenominatorText_proper = undefined;
		//
		nume = "";
		dashLine = "";
		deno = "";
		//
		display(true, "0");
		this.clear = true;
		//
	}
	//////////////////////////////////////////////////////////////////
	private function AllClear() {
		calculation = "";
		this.value = "0";
		isstart = true;
		this.operator = "";
		isdec = false;
		this.clear = true;
		this.stack.length = 0;
		this.ansValue = 0;
		//
		nume = "";
		dashLine = "";
		deno = "";
		//
		focusDenominatorText_proper = undefined;
		focusNumeratorText = undefined;
		focusDenominatorText = undefined;
		FD_toggle = undefined;
		CF_toggle = undefined;
		//
		display(true, this.value);
		valueHolder = this.value;
		//
	}
	////////////////////////////////////////////////////////////////////////
	private function display(isDisplay:Boolean, displayValue:String) {
		operatorTxt.text = this.operator;
		memoryTxt.text = this.mem;
		if (isDisplay) {
			displayTxt.text = displayValue;
		}
		//                                                                    
		//
		if (isFraction) {
			numeTxt.text = (nume).toString();
			denoTxt.text = (deno).toString();
			dashLineTxt.text = dashLine;
		}
	}
	/////////////////////////////////////////////////////////////////////////
	private function SpecialOperations(_str:String) {
		switch (_str) {
		case "%R" :
			//
			trace("When %R is Pressed !!");
			display(true, valueHolder);
			numToBeDivided = valueHolder;
			operatorTxt.text = "%R";
			this.stack.length = 0;
			this.clear = true;
			last_keyPressed_is_PR = true;
			//
			break;
			//
		case "%" :
			//
			this.value = (Number(valueHolder)/100).toString();
			display(true, this.value);
			valueHolder = this.value;
			//
			break;
			//
		case "sqrt" :
			//
			this.value = Math.sqrt(Number(valueHolder)).toString();
			display(true, this.value);
			valueHolder = this.value;
			//
			break;
			//
		case "(" :
			//
			if (last_keyPressed_is_operator) {
				bracketsOpened = true;
				//
				display(true, "(");
				//
				trace("--------=== Bracket Opened ===-------------");
				//
				for (var i = 0; i<this.stack.length; i++) {
					tempStack[i] = this.stack[i];
				}
				//
				trace("this.stack == "+this.stack+" | tempStack == "+tempStack);
				this.stack.length = 0;
				this.value = valueHolder;
				//
				trace(" this.stack == "+this.stack);
				trace(" valueHolder == "+valueHolder);
			}
			//                                            
			break;
			//
		case ")" :
			//
			if ((bracketsOpened) && (!last_keyPressed_is_operator)) {
				bracketsClosed = true;
				//
				trace("--------=== Bracket Closed ===-------------");
				this.stack.push(valueHolder);
				trace("this.stack ...... "+this.stack);
				//
				var newValue = "";
				for (var i = 0; i<this.stack.length; i++) {
					newValue += this.stack[i];
				}
				trace("newValue == "+newValue);
				this.ansValue = _parent.evals(newValue);
				//
				tempStack.push(this.ansValue);
				trace(" tempStack == "+tempStack);
				//
				var newValue = "";
				for (var i = 0; i<tempStack.length; i++) {
					newValue += tempStack[i];
				}
				trace("newValue == "+newValue);
				this.value = _parent.evals(newValue);
				valueHolder = this.value;
				trace("valueHolder === "+valueHolder);
				//
				display(true, valueHolder);
				//
				tempStack.length = 0;
				this.stack.length = 0;
				this.stack.push(valueHolder);
				this.clear = true;
				trace("this.stack == "+this.stack);
				trace("**********************************\n");
			}
			//                                            
			break;
			//
		case "a" :
			//
			trace("Pressed 'a' ...........");
			if ((!determineIfDecimal(Number(valueHolder)) && (displayTxt.length<=4))) {
				isFraction = true;
				this.clear = true;
				focusNumeratorText = true;
				CF_toggle = true;
			}
			//                                                                      
			break;
			//
		case "b/c" :
			//
			trace("Pressed 'b/c' ...........");
			if ((!determineIfDecimal(Number(valueHolder)) && (displayTxt.length<=4))) {
				isFraction = true;
				this.clear = true;
				focusDenominatorText = true;
				FD_toggle = true;
				//
				if (numeTxt.text.length>1) {
					focusDenominatorText_proper = true;
				}
				//          
			}
			//                                                                      
			break;
			//
		case "CF_SF" :
			//
			trace("\nInside  :::: CF_SF");
			if ((Number(valueHolder)) != 0 && (FD_toggle)) {
				if (CF_toggle) {
					//Input Format : W-N-D
					trace("Input Format : W-N-D ");
					trace("fractValue == "+fractValue+" NumeratorValue == "+NumeratorValue+" DenominatorValue == "+DenominatorValue);
					//
					isFraction = true;
					//
					nume = (WND_To_ND(fractValue, NumeratorValue, DenominatorValue).numerator).toString();
					deno = (WND_To_ND(fractValue, NumeratorValue, DenominatorValue).denominator).toString();
					dashLine = "___";
					//
					this.value = (fraction_To_decimal(Number(nume), Number(deno)).decimal).toString();
					display(true, "");
					valueHolder = this.value;
					//
					CF_toggle = false;
					FD_toggle = true;
				} else {
					//Input Format : N-D
					trace("Input Format : N-D ");
					trace("nume == "+nume+" deno == "+deno);
					//
					isFraction = true;
					//
					this.value = (fraction_To_decimal(Number(nume), Number(deno)).decimal).toString();
					//
					fractValue = ND_To_WND(Number(nume), Number(deno)).whole;
					NumeratorValue = ND_To_WND(Number(nume), Number(deno)).numerator;
					DenominatorValue = ND_To_WND(Number(nume), Number(deno)).denominator;
					//
					nume = (NumeratorValue).toString();
					deno = (DenominatorValue).toString();
					dashLine = "___";
					//
					display(true, fractValue.toString());
					valueHolder = this.value;
					//
					CF_toggle = true;
				}
			}
			//                                                              
			break;
			//
		case "F_D" :
			//
			trace("\nInside  :::: F_D");
			if (FD_toggle) {
				//Input Format : N-D
				trace("Input Format : N-D ");
				trace("nume == "+nume+" deno == "+deno);
				//
				isFraction = true;
				//
				this.value = (fraction_To_decimal(Number(nume), Number(deno)).decimal).toString();
				nume = "";
				dashLine = "";
				deno = "";
				display(true, this.value);
				valueHolder = this.value;
				//
				FD_toggle = false;
				//isFraction = false;
			} else {
				//Input Format : Decimal
				trace("Input Format : Decimal ");
				trace("~~~~~valueHolder == "+valueHolder);
				//
				isFraction = true;
				//
				trace("---- Before Calling ---");
				nume = (decimal_To_fraction(Number(valueHolder)).nd_numerator).toString();
				deno = (decimal_To_fraction(Number(valueHolder)).nd_denominator).toString();
				dashLine = "___";
				//
				if((nume == undefined || nume == NaN) && (deno == undefined || deno == NaN)){
					nume = "";
					deno = "";
					dashLine = "";
				}
				//
				trace("@@@---- nume == "+nume+" deno == "+deno);
				//
				this.value = (fraction_To_decimal(Number(nume), Number(deno)).decimal).toString();
				trace(" this.value == "+this.value);
				//
				if(this.value == undefined || this.value == "NaN"){
					display(true, valueHolder);
					this.value = valueHolder;
				}
				else{
					display(true, "");
					valueHolder = this.value;
					//
					FD_toggle = true;
					CF_toggle = false;
				}
				//
				trace(" this.value == "+this.value);
				trace(" valueHolder === "+valueHolder)
				//
			}
			//   
			break;
			//
		case "simplify" :
			//
			if (isFraction) {
				trace("\nInside  :::: Simplify");
				if (CF_toggle) {
					//Input Format : W-N-D
					trace("Input Format : W-N-D ");
					trace("fractValue == "+fractValue+" NumeratorValue == "+NumeratorValue+" DenominatorValue == "+DenominatorValue);
					//
					isFraction = true;
					//
					var num = (WND_To_ND(fractValue, NumeratorValue, DenominatorValue).numerator).toString();
					var den = (WND_To_ND(fractValue, NumeratorValue, DenominatorValue).denominator).toString();
					//
					this.value = (fraction_To_decimal(Number(num), Number(den)).decimal).toString();
					//
					fractValue = simplify_fraction(Number(num), Number(den)).sipm_wnd_whole;
					NumeratorValue = simplify_fraction(Number(num), Number(den)).sipm_wnd_numerator;
					DenominatorValue = simplify_fraction(Number(num), Number(den)).sipm_wnd_denominator;
					//
					nume = (NumeratorValue).toString();
					deno = (DenominatorValue).toString();
					dashLine = "___";
					//
					display(true, fractValue.toString());
					operatorTxt.text = "simp";
					valueHolder = this.value;
					//
				} else {
					//Input Format : N-D
					trace("Input Format : N-D");
					trace("nume == "+nume+" deno == "+deno);
					//
					isFraction = true;
					//
					this.value = (fraction_To_decimal(Number(nume), Number(deno)).decimal).toString();
					//
					var num = nume;
					var den = deno;
					//
					nume = (simplify_fraction(Number(num), Number(den)).sipm_numerator).toString();
					deno = (simplify_fraction(Number(num), Number(den)).sipm_denominator).toString();
					dashLine = "___";
					//
					trace("$$$$** Nume == "+nume+" Deno == "+deno);
					//
					display(true, "");
					operatorTxt.text = "simp";
					valueHolder = this.value;
					//
					FD_toggle = true;
				}
			}
			//                      
			break;
		}
		trace(":::::::::::: OUT :::::::::::::");
	}
	///////////////////////////////////////////////////////////////////////////////
	private function calculationBeforeOperatorPressed() {
		trace("\n******************************\nInside calculateBeforOperation "+this.stack);
		//
		if (isFraction || isMemoryOperation) {
			this.value = valueHolder;
		}
		this.stack.push(this.value);
		//
		var newValue = "";
		for (var i = 0; i<this.stack.length; i++) {
			newValue += this.stack[i];
		}
		trace("newValue == "+newValue);
		// 
		this.ansValue = _parent.evals(newValue);
		//
		display(true, this.value);
		if ((valueHolder != "-Infinity") || (valueHolder != "Infinity")) {
			valueHolder = String(this.ansValue);
		}
		//  
		trace("this.value ----> "+this.value);
		trace(" Stack ---> "+this.stack);
		trace("Displayed Value ---> "+valueHolder);
		//
		var limit = this.stack.length;
		stack_secondLastElement = this.stack[limit-2];
		stack_lastElement = this.stack[limit-1];
		//
		this.stack.length = 0;
		this.stack.push(valueHolder);
		//
		//last_keyPressed_is_operator = false;
		//last_keyPressed_is_equal = false;
		//
		trace(" Stack === "+this.stack);
		trace("Displayed Value === "+this.value);
		trace(" valueHolder  === "+valueHolder);
		trace("*************************************");
	}
	//**************************************************************************************
	var accuracy:Number = 0.00001;
	private function decimal_To_fraction(decimal:Number):Object {
		//
		trace(" Inside decimal_To_fraction ");
		trace(" *** decimal === "+decimal);
		//
		if(decimal >= 0){
			//Variable Declarations
			var answerObj:Object = new Object();
			var hasWhole:Boolean = false;
			var answer_ND_nmtr:Number;
			var answer_ND_dntr:Number;
			var answer_WND_whole:Number;
			var answer_WND_dntr:Number;
			var answer_WND_nmtr:Number;
			//
			trace("Entered Decimal Number :: "+decimal);
			// Calulation Formulae
			if (decimal>=0) {
				hasWhole = true;
				answer_WND_whole = (Math.floor(decimal));
			}
			if (decimal-Math.floor(decimal) == 0) {
				answer_WND_nmtr = 0;
				answer_WND_dntr = 1;
			} else if (hasWhole) {
				decimal = decimal-Math.floor(decimal);
			}
			var a:Number = decimal-int(decimal);
			var p:Number = 0;
			var q:Number = a;
			while (Math.abs(q-Math.round(q))>accuracy) {
				p++;
				q = p/a;
				answer_WND_nmtr = (Math.round(q*decimal));
				answer_WND_dntr = (Math.round(q));
			}
			//
			trace("answer_WND_dntr == "+answer_WND_dntr);
			trace("answer_WND_whole == "+answer_WND_whole);
			trace("answer_WND_nmtr == "+answer_WND_nmtr);
			//
			answer_ND_nmtr = (answer_WND_dntr*answer_WND_whole)+answer_WND_nmtr;
			answer_ND_dntr = answer_WND_dntr;
			// Answer Assignments
			answerObj.wnd_whole = answer_WND_whole;
			answerObj.wnd_numerator = answer_WND_nmtr;
			answerObj.wnd_denominator = answer_WND_dntr;
			//
			answerObj.nd_numerator = answer_ND_nmtr;
			answerObj.nd_denominator = answer_ND_dntr;
			//
			trace("answer_ND_nmtr == "+answer_ND_nmtr+"  answerObj.nd_numerator == "+answerObj.nd_numerator);
			return answerObj;
		}
	}
	//*********************************************************************************
	private function fraction_To_decimal(nmtr:Number, dntr:Number):Object {
		//Variable Declarations
		var answerObj:Object = new Object();
		var answer_decimal:Number;
		// Calulation Formulae
		answer_decimal = (nmtr/dntr);
		// Answer Assignments
		answerObj.decimal = answer_decimal;
		//
		return answerObj;
	}
	//*********************************************************************************
	private function WND_To_ND(whole:Number, nmtr:Number, dntr:Number):Object {
		//Variable Declarations
		var answerObj:Object = new Object();
		var answer_nmtr:Number;
		var answer_dntr:Number;
		// Calulation Formulae
		answer_nmtr = (dntr*whole)+nmtr;
		answer_dntr = dntr;
		// Answer Assignments
		answerObj.numerator = answer_nmtr;
		answerObj.denominator = answer_dntr;
		//
		return answerObj;
	}
	//*********************************************************************************
	private function ND_To_WND(nmtr:Number, dntr:Number):Object {
		//Variable Declarations
		var answerObj:Object = new Object();
		var answer_whole:Number;
		var answer_nmtr:Number;
		var answer_dntr:Number;
		// Calulation Formulae
		answer_whole = Math.floor(nmtr/dntr);
		answer_nmtr = (nmtr%dntr);
		answer_dntr = dntr;
		// Answer Assignments
		answerObj.whole = answer_whole;
		answerObj.numerator = answer_nmtr;
		answerObj.denominator = answer_dntr;
		//
		return answerObj;
	}
	//*********************************************************************************
	private function fractionAddition(nmtr_A:Number, dntr_A:Number, nmtr_B:Number, dntr_B:Number):Object {
		// Variable Declarations
		var answerObj:Object = new Object();
		// For ND format
		var answer_nmtr:Number;
		var answer_dntr:Number;
		// For WND format
		var answer_whole:Number;
		var answer_frN:Number;
		var answer_frD:Number;
		// Calulation Formulae
		if (dntr_A == dntr_B) {
			answer_nmtr = (nmtr_A+nmtr_B);
			answer_dntr = dntr_A;
		} else if (dntr_A%dntr_B == 0) {
			answer_nmtr = nmtr_A+((dntr_A/dntr_B)*nmtr_B);
			answer_dntr = dntr_A;
		} else if (dntr_B%dntr_A == 0) {
			answer_nmtr = nmtr_B+((dntr_B/dntr_A)*nmtr_A);
			answer_dntr = dntr_B;
		} else {
			answer_nmtr = (nmtr_A*dntr_B)+(nmtr_B*dntr_A);
			answer_dntr = (dntr_A*dntr_B);
		}
		//
		answer_whole = Math.floor(answer_nmtr/answer_dntr);
		answer_frN = (answer_nmtr%answer_dntr);
		answer_frD = answer_dntr;
		// Answer Assignments
		answerObj.wnd_whole = answer_whole;
		answerObj.wnd_numerator = answer_frN;
		answerObj.wnd_denominator = answer_frD;
		//
		answerObj.nd_numerator = answer_nmtr;
		answerObj.nd_denominator = answer_dntr;
		//
		return answerObj;
	}
	//*********************************************************************************
	private function fractionSubtraction(nmtr_A:Number, dntr_A:Number, nmtr_B:Number, dntr_B:Number):Object {
		// Variable Declarations
		var answerObj:Object = new Object();
		// For ND format
		var answer_nmtr:Number;
		var answer_dntr:Number;
		// For WND format
		var answer_whole:Number;
		var answer_frN:Number;
		var answer_frD:Number;
		// Calulation Formulae
		if (dntr_A == dntr_B) {
			answer_nmtr = (nmtr_A-nmtr_B);
			answer_dntr = dntr_A;
		} else if (dntr_A%dntr_B == 0) {
			trace(" DEN B is bigger");
			answer_nmtr = nmtr_A-((dntr_A/dntr_B)*nmtr_B);
			answer_dntr = dntr_A;
		} else if (dntr_B%dntr_A == 0) {
			trace(" DEN A is bigger");
			answer_nmtr = ((dntr_B/dntr_A)*nmtr_A)-nmtr_B;
			answer_dntr = dntr_B;
		} else {
			answer_nmtr = (nmtr_A*dntr_B)-(nmtr_B*dntr_A);
			answer_dntr = (dntr_A*dntr_B);
		}
		//
		answer_whole = Math.floor(answer_nmtr/answer_dntr);
		answer_frN = Math.abs(answer_nmtr%answer_dntr);
		answer_frD = answer_dntr;
		// Answer Assignments
		answerObj.wnd_whole = answer_whole;
		answerObj.wnd_numerator = answer_frN;
		answerObj.wnd_denominator = answer_frD;
		//
		answerObj.nd_numerator = answer_nmtr;
		answerObj.nd_denominator = answer_dntr;
		//
		return answerObj;
	}
	//*********************************************************************************
	private function simplify_fraction(nmtr:Number, dntr:Number):Object {
		//Variable Declarations
		var answerObj:Object = new Object();
		// For ND format
		var answer_nmtr:Number;
		var answer_dntr:Number;
		// For WND format
		var answer_whole:Number;
		var answer_frN:Number;
		var answer_frD:Number;
		//
		var GCF:Number;
		// Calulation Formulae
		GCF = get_common_divisor(nmtr, dntr);
		//
		trace("GCF is :: "+GCF);
		//
		answer_nmtr = (nmtr/GCF);
		answer_dntr = (dntr/GCF);
		//
		answer_whole = Math.floor(answer_nmtr/answer_dntr);
		answer_frN = Math.abs(answer_nmtr%answer_dntr);
		answer_frD = answer_dntr;
		// Answer Assignments
		answerObj.sipm_wnd_whole = answer_whole;
		answerObj.sipm_wnd_numerator = answer_frN;
		answerObj.sipm_wnd_denominator = answer_frD;
		//
		answerObj.sipm_numerator = answer_nmtr;
		answerObj.sipm_denominator = answer_dntr;
		//
		//
		return answerObj;
	}
	//
	function get_common_divisor(a:Number, b:Number) {
		while (a != 0 && b != 0) {
			if (a>b) {
				a = a%b;
			} else {
				b = b%a;
			}
		}
		if (a == 0) {
			return b;
		} else {
			return a;
		}
	}
	//*********************************************************************************
	private function determineIfDecimal(_num:Number):Boolean {
		var result_num:Number = _num - Math.floor(_num);
		//
		if (result_num>0) {
			return true;
		} else {
			return false;
		}
	}
	//*********************************************************************************
}
