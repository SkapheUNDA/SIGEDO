
function UpdateGraphviz() {
	try {
		var svg_div = jQuery('#graphviz_svg_div');
		var graphviz_data_textarea = jQuery('#textoFlujo');
		svg_div.html("");
		var data = graphviz_data_textarea.text();
		// Generate the Visualization of the Graph into "svg".
		var svg = Viz(data, "svg");
		svg_div.html(svg);
	} catch (e) {
		alert("Error generando flujo "+e);
	}
}

