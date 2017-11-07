
function setFocus(id) {


    var element = document.getElementById(id);
    if (element && element.focus) {
         element.focus();
    }
}

function setScroll(scroll) {
	if(scroll != null && scroll != 0){
	window.scrollBy(0,scroll); 
	}
}

function selectAll(checkAll) {
    var checked = checkAll.checked; 

    //to change the checked attribute                
    $(':checkbox[id*="test"]').attr('checked', checked);

    if (checked) {
        $('div[id*="test"] > div').each(function() {
            $(this).addClass('ui-state-active');
            $(this).children('span').addClass('ui-icon ui-icon-check');
        });
    } else {
        $('div[id*="test"] > div').each(function() {
            $(this).removeClass('ui-state-active');
            $(this).children('span').removeClass('ui-icon ui-icon-check');
        });
    }
}
	