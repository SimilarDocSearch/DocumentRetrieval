<!DOCTYPE html>
<!---
   @author hpandav 
-->

<html>
<head>
<%@include file="Header.jsp"%>
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

	<div id="showAlert" style="display: none;"
		class="bb-alert alert alert-info">
		<span></span>
	</div>
	<%@include file="NavBar.jsp"%>
	<div class="loader"></div>

	<div id="main">

		<input type="hidden" value="false" id="resetForm" />
		<div id="menu">
			<div id="content" style="text-align: center">
				<div class="row vertical-center-row">
					<div class="col-lg-7 col-lg-offset-2">
						<div class="row ">
							<div style="text-align: center">
								<form class="form-horizontal">
									<div class="form-group">
										<label for="inputEmail" class="control-label col-xs-2">Enter
											Search Query</label>
										<div class="col-xs-10">
											<input type="text" class="form-control" id="inputEmail">
										</div>
									</div>
									<div class="form-group">
										<label for="numberOfDocs" class="control-label col-xs-2">Enter
											Number of Documents</label>
										<div class="col-xs-2">
											<input type="number" class="form-control" id="numberOfDocs">
										</div>
									</div>
									<div class="form-group">
										<label for="rankingMethod" class="control-label col-xs-2">Select
											Ranking Method</label>
										<div class="col-xs-3">
											<select class="form-control" id="rankingMethod">
												<option value="cosine">Cosine Similarity</option>
												<option value="subrange">Subrange-Based</option>
												<option value="fastSim">Fast-Similarity</option>
											</select>
										</div>
									</div>

									<div class="form-group">
											<button type="button" class="btn btn-primary" onclick="getSearchResult()">
												<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
												Search
											</button>

									</div>
								</form>
							</div>
						</div>
						
					</div>
					
				</div>
				<hr>
<fieldset>
					<div class="row">
						<div class="col-sm-5">
							<div class="row-fluid sortable"> 
								<div class="box span12"> 
									<div class="box-header" data-original-title=""> 
									<h3><i class="halflings-icon edit"></i>
									<span class="break"></span>Ranked Databases</h3> 
									</div> 
									<hr><div class="box-content">
						
							<table id="rankedDBs" class="table">
								
							</table>
							</div></div></div>
						</div>
												<div class="col-sm-1"></div>
						
						<div class="col-sm-6">
							<div class="row-fluid sortable"> 
								<div class="box span12"> 
									<div class="box-header" data-original-title=""> 
									<h3><i class="halflings-icon edit"></i>
									<span class="break"></span>Most Similar Documents</h3> 
									 </div> 
									 <hr>
									 <div class="box-content">
						
							<table id="docList" class="table">
								
							</table>
							</div></div></div>
						</div>
					</div>
				</fieldset>
			</div>
		</div>
	</div>
	<!-- 	</form> -->
</body>
</html>