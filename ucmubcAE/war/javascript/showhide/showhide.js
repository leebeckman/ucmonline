//http://woork.blogspot.com/2008/03/two-css-vertical-menu-with-showhide.html
//Antonio Lupetti
window.addEvent('domready', function(){
    var myMenu= new Fx.Slide('v-menu');
    myMenu.hide();
    $('toggle').addEvent('click', function(e){
        e = new Event(e);
        myMenu.toggle();
        e.stop();
    });
}); 