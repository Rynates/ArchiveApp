<#import "common/bootstrap.ftl" as b>

<@b.page>
    <#if archiveId?? || funds??>
        <input type="hidden" name="archiveId" value="${archiveId}">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Number</th>
                <th>Name</th>
                <th>Inventory</th>
            </tr>
            <div class="form-outline mb-4">
              <input type="search" class="form-control" id="datatable-search-input">
              <label class="form-label" for="datatable-search-input">Search</label>
            </div>
            <div id="datatable">
            </div>
            </thead>
            <tbody>
            <#list funds as fund>
                <tr>
                    <td style="vertical-align:middle"><h4>${fund.number}</h4></td>
                    <td style="vertical-align:middle"><h4>${fund.name}</h4></td>
                    <td style="vertical-align:middle"><h4>${fund.inventoryNum}</h4></td>
                    <td class="col-md-1" style="text-align:center;vertical-align:middle;">
                        <form method="post" action="/funds">
                            <input type="hidden" name="action" value="inventories">
                            <input type="hidden" name="fundId" value="${fund.id}">
                            <input type="image" src="/static/papka.png" width="24" height="24" border="0"
                                   alt="Inventories">
                            <a href="{{url_for('/inventories'), parameter = '${fund.id}'}}"></a>
                        </form>
                    </td>
                    <td class="col-md-1" style="text-align:center;vertical-align:middle;">
                        <form method="post" action="/funds">
                            <input type="hidden" name="date" value="${date?c}">
                            <input type="hidden" name="id" value="${fund.id}">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="archiveId" value="${archiveId}">
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
                       aria-expanded="false" aria-controls="multiCollapseExample1">Insert new fund & inventory</a>
                </td>
            </div>
    <div class="collapse multi-collapse" id="multiCollapseExample1">
        <div class="col-md-2">
            <form method="post" action="/funds">
                <input type="hidden" name="archiveId" value="${archiveId}">
                <input type="hidden" name="date" value="${date?c}">
                <input type="hidden" name="action" value="add">
                Number </br>
                <input type="text" class="form-control" name="number" required/>
                </br> </br>
                Name </br>
                <input type="text" class="form-control" name="name" required/>
                </br> </br>
                Inventory </br>
                <input type="text" class="form-control" name="inventor" required/>
                </br> </br>
                Digitization </br>
                <input type="text" class="form-control" name="digitization"/>
                </br> </br>
                <input type="submit" class="btn btn-primary" value="Submit"/>
            </form>
        </div>
    </div>

</@b.page>