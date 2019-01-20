//call ajax
		var ajax = new XMLHttpRequest();
		
		ajax.open("GET", "data.php", true);
		
		ajax.send();
		
		// receiving response from data.php
		ajax.onreadystatechange = function(){
			if(this.readyState == 4 && this.status == 200){
				// converting JSON back to array
				var data = JSON.parse(this.responseText);
				console.log(data); // for debugging
				
				//html values
				var html = "";
				
				for(var a = 0; a < data.length; a++){
					var id = data[a].id;
					var freq = data[a].frequency;
					var loc = data[a].location;
					var dat = data[a].date;
					
					//appending at html
					html += "<tr>";
						html += "<td>" + id + "</td>";
						html += "<td>" + freq + "</td>";
						html += "<td>" + loc + "</td>";
						html += "<td>" + dat + "</td>";
					html += "</tr>";
				}
				
				//replacing the <tbody> of <table>
				document.getElementById("data").innerHTML = html;
				//document.getElementById("data").innerHTML = html.style
				
			}
		}