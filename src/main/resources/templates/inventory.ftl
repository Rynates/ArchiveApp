<#import "common/bootstrap.ftl" as b>

<@b.page>
    <#if fundId?? || inventories??>
        <input type="hidden" name="fundId" value="${fundId}">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Number</th>
                <th>Name</th>
            </tr>
            </thead>
            <tbody>
            <#list inventories as inventory>
                <tr>
                    <td style="vertical-align:middle"><h4>${inventory.number}</h4></td>
                    <td style="vertical-align:middle"><h4>${inventory.name}</h4></td>
<#--                    //<td style="vertical-align:middle"><h4>${inventory.years}</h4></td>-->
                    <td class="col-md-1" style="text-align:center;vertical-align:middle;">
                    </td>
                     <td class="col-md-1" style="text-align:center;vertical-align:middle;">
                                            <form method="post" action="/inventories" enctype="multipart/form-data">
                                                <input type="hidden" name="date" value="${date?c}">
                                                <input type="hidden" name="id" value="${inventory.id}">
                                                <input type="file" name="action" value="document">
                                                <input type="hidden" name="fundId" value="${fundId}">
                                            </form>
                                        </td>
                    <td class="col-md-1" style="text-align:center;vertical-align:middle;">
                        <form method="post" action="/inventories">
                            <input type="hidden" name="date" value="${date?c}">
                            <input type="hidden" name="id" value="${inventory.id}">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="fundId" value="${fundId}">
                            <input type="image" src="/static/delete.png" width="24" height="24" border="0">
                        </form>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </#if>

 <div class="panel-body">
                <td class="col-md-1" style="text-align:center;vertical-align:middle;">
                    <a class="btn btn-primary" data-toggle="collapse" href="#multiCollapseExample1" role="button"
                       aria-expanded="false" aria-controls="multiCollapseExample1">Insert new document </a>
                </td>
            </div>
    <div class="collapse multi-collapse" id="multiCollapseExample1">
        <form method="post" action="/inventories">
            <input type="hidden" name="fundId" value="${fundId}">
            <input type="hidden" name="date" value="${date?c}">
            <input type="hidden" name="action" value="add">
            Number </br>
            <input type="text" name="number" required/>
            </br> </br>
            Name </br>
            <input type="text" name="name" required/>
            </br> </br>
<#--            Inventory </br>-->
<#--            <input type="text" name="inventor" required/>-->
<#--            </br> </br>-->
            <input type="submit" class="btn btn-primary btn-lg" value="Submit"/>
        </form>
    </div>
</@b.page>