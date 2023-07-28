<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "common/bootstrap.ftl" as b>

<@b.page>
  <div class="row">
    <div class="col-md-4 col-md-offset-4">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Log In</h3>
        </div>
        <div class="panel-body">
          <form action="/log_in" method="POST">
<#--              <#if error??>-->
<#--                <p>${error}</p>-->
<#--              </#if>-->

              <div class="form-group">
                <label for="userId">Username</label>
                <input type="text" name="userId" class="form-control" id="userId" placeholder="Username">
              </div>
              <div class="form-group">
                <label for="password">Password</label>
                <input type="password" name="password" class="form-control" id="password" placeholder="Password">
              </div>
              <button type="submit" class="btn btn-primary btn-lg">Log In</button>
            </form>
        </div>
      </div>
    </div>
  </div>
</@b.page>

