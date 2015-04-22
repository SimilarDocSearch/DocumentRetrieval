<!DOCTYPE html>
<!---
   @author hpandav 
-->

<html>
<head>
<%@include file="Header.jsp" %>
<script type="text/javascript">


window.onbeforeunload = function(e) {
	$(".loader").fadeIn("fast");

}
window.onload = function() {

	$(".loader").fadeOut("slow");

}
</script>
</head>
<body id="home">

	<div id="showAlert" style="display: none;" class="bb-alert alert alert-info" >
        <span></span>
    </div>
<%@include file="NavBar.jsp" %>
<div class="loader"></div>

		<div id="main">
			<button id = "saveBut" class="btn btn-success" onclick="saveForm();">
  				<i class="glyphicon glyphicon-floppy-disk"></i> Save
			</button>
			<button id = "resetBut" class="btn btn-danger" onclick="resetForm();">
			  <i class="glyphicon glyphicon-refresh"></i> Reset
			</button>
			<input type="hidden" value="false" id="resetForm"/>
			<div id="menu">
				<div id="content">
					<div style="width: 100%; overflow: auto;">
						<div style="float: left; width: 77%">
								<fieldset style="margin-bottom: 5px;">
									<label for="mIDDesc">Mailing ID Desc</label>
									<input type=text id="mIDDesc" style="margin-left: 10px;">
								</fieldset>
							<div id="posCodeContainer" style="position: relative; overflow: hidden;">
								<fieldset>
									<label for="posCode">Position Code</label> 
									
									<select
										style="width: 300px; margin-left: 20px" id="posCode"
										name="posCodeSel" onchange="positionCodeSel();">
										<option style="width: 400px" value ="select" selected>Select</option>

	
									</select>
								</fieldset>
								<fieldset>
									<label for="pCount">Preview Count</label>
									<input type=text id="pCount" disabled>
								</fieldset>
								<fieldset>
								<label for="tCount">Total Count</label>
								<input type=text id="tCount" disabled>
							</fieldset>
							</div>
							<div>
								<fieldset>
								    <button name="addToGroup" id="addToGroup" onclick="javascript:addToGrpSel();">Add to Group Selection</button>
								    <button name="showNames" id="getPatrons" onclick="javascript:getPatronList();">Show Names --></button>
								</fieldset>
								<div style="margin-top:20px; margin-left:7px">
									<fieldset>
										<label for="grpSelDiv">Group Selection</label>
										<div id="grpSelDiv">
											<ul id="grpSelList" data-role="listview">

											</ul>
										</div>
									</fieldset>
								</div>
							</div>							
							<div id="materialSelContainer" style="position: relative; overflow: hidden; margin-top: 20px">
								<fieldset>
									<label for="materialSel">Materials Available</label> 
									
									<select
										style="width: 300px; margin-left: 20px" id="materialSel"
										name="materialSel">
										<option style="width: 400px" value ="select" selected>Select</option>
	
									</select>
								</fieldset>								
							</div>
							<div>
								<fieldset>
								    <button name="addMatToSel" id="addMatToSel" onclick="javascript:addMatToSel();">Add Material to Selection</button>
								</fieldset>
								<div style="margin-top:20px; margin-left:7px">
									<fieldset>
										<label for="matSelList">Material Selection</label>
										<div id="matSelDiv">
											<ul id="matSelList" data-role="listview">

											</ul>
										</div>
									</fieldset>
								</div>
							</div>
							
							
						</div>
						<div>
							<fieldset>
								<label for="patronsDiv">Patrons</label>
								<div id="patronsDiv">
									<ul id="patronList" data-role="listview">
									</ul>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
			</div>
		</div>
<!-- 	</form> -->
</body>
</html>