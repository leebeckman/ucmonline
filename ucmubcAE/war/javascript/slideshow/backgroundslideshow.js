  var pagebanners = new Array();
  
  function startBanner() {
	showNextSlide();
	if (pagebanners.length > 1) {
		setInterval("showNextSlide()", 10000);
	}
  }
  
  function showNextSlide() {
    newpath = pagebanners.pop();
    swapImage(newpath, "mainbanner", 500);
    pagebanners.unshift(newpath);
  }
  
  function changePos(element, pos) { 
    element.style.backgroundPosition = "0 " + pos;
  }
  
  function swapImage(newpath, target, millisec) { 
    var speed = Math.round(millisec / 100); 
    var timer = 0; 
    
    element = document.getElementById(target);
    
    //image fly out
    for(i = 0; i <= 200; i+=5) {
      setTimeout("changePos(element," + i + ")",(timer * speed)); 
      timer++; 
    } 
    
    timer += 5;
    setTimeout("element.style.backgroundImage = 'url(" + newpath + ")'", (timer * speed));
    
    //image fly in
    for(i = -200; i <= 0; i+=5) {
      setTimeout("changePos(element," + i + ")",(timer * speed)); 
      timer++; 
    } 

  }