<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<br/>
<br/>
<br/>
<form>
	<div class="row">
		<div class="col-xs-12 col-md-2">
			<strong>rdd</strong>
			<div class="form-group">
				<div class="input-group date" id="cartrdddatetimepicker">
					<input type="text" class="form-control" />
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
		</div>
		<div class="col-xs-12 col-md-2">
			<strong>po</strong>
			<div>
				<input type="text" class="form-control" style="width: 99%;" />
			</div>
		</div>
		<div class="col-xs-12 col-md-8">
			<strong>notes</strong>
			<div>
				<textarea class="form-control" style="width: 400px; min-height: 100px;"></textarea>
			</div>
		</div>
	</div>
</form>