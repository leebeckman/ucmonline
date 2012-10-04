sfHover = function() {
	var sfEls = document.getElementById("navigationbar").getElementsByTagName("LI");
	for (var i=0; i<sfEls.length; i++) {
		sfEls[i].onmouseover=function() {
			this.className+=" sfhover";
		}
		sfEls[i].onmouseout=function() {
			this.className=this.className.replace(new RegExp(" sfhover\\b"), "");
		}
	}
	
	if (document.getElementById("bottombuttons") != null) {
		var bottomButtons = document.getElementById("bottombuttons").getElementsByTagName("DIV");
	
		for (var i=0; i<bottomButtons.length; i++) {
			bottomButtons[i].onmouseover=function() {
				this.className+=" sfhover";
			}
			bottomButtons[i].onmouseout=function() {
				this.className=this.className.replace(new RegExp(" sfhover\\b"), "");
			}
		}
	}
}
if (window.attachEvent) window.attachEvent("onload", sfHover);