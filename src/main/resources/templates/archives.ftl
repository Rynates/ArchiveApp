<#import "common/bootstrap.ftl" as b>

<@b.page>
    <#if archives?? && (archives?size > 0)>

        <table class="table table-striped">
            <thead>

            <tr>
                <th>Name</th>
                <th>City</th>
                <th>Type</th>
                <th>Phone</th>
                <th>Address</th>
                <th>Email</th>
                <th>List of funds</th>
            </tr>
            </thead>
            <tbody>
            <#list archives as archive>
                <tr>
                    <td style="vertical-align:middle"><h4>${archive.name}</h4></td>
                    <td style="vertical-align:middle"><h4>${archive.city}</h4></td>
                    <td style="vertical-align:middle"><h4>${archive.type}</h4></td>
                    <td style="vertical-align:middle"><h4>${archive.phone}</h4></td>
                    <td style="vertical-align:middle"><h4>${archive.address}</h4></td>
                    <td style="vertical-align:middle"><h4>${archive.email}</h4></td>
                    <td class="col-md-1" style="text-align:center;vertical-align:middle;">
                        <form method="post" action="/archives">
                        <input type="hidden" name="action" value="funds">
                        <input type="hidden" name="archiveId" value="${archive.id}">
                            <input type="image" src="/static/papka.png" width="24" height="24" border="0" alt="Funds">
                            <a href = "{{url_for('/funds'), parameter = '${archive.id}'}}"></a>
                        </form>
                    </td>
                    <td class="col-md-1" style="text-align:center;vertical-align:middle;">
                        <form method="post" action="/archives">
                            <input type="hidden" name="date" value="${date?c}">
                            <input type="hidden" name="id" value="${archive.id}">
                            <input type="hidden" name="action" value="delete">
                            <input type="image" src="/static/delete.png" width="24" height="24" border="0 alt=" Delete">
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
                       aria-expanded="false" aria-controls="multiCollapseExample1">Insert new archive </a>
                </td>
            </div>
    <div class="collapse multi-collapse" id="multiCollapseExample1">
        <form method="post" action="/archives">
            <input type="hidden" name="date" value="${date?c}">
            <input type="hidden" name="action" value="add">
            Name </br>
            <input type="text" name="name" required/>
            </br> </br>
            City </br>
            <input type="text" name="city" required/>
            </br> </br>
            Type: </br>
            <div class="form-check">
                <input class="form-check-input" type="radio" name="type" value="Зональный">
                <label class="form-check-label" for="flexRadioDefault1">
                    Зональный
                </label>
            </div>
            <div class="form-check">
                <input class="form-check-input" type="radio" name="type" value="Республиканский" checked>
                <label class="form-check-label" for="flexRadioDefault2">
                    Республиканский
                </label>
            </div>
            </br> </br>
            Phone: </br>
            <input type="text" name="phone" required/>
            </br> </br>
            Address: </br>
            <input type="text" name="address" required/>
            </br> </br>
            Email: </br>
            <input type="text" name="email" required/>
            </br> </br>
            </br>
            <input type="submit" class="btn btn-primary btn-lg" value="Submit"/>
        </form>
    </div>

</@b.page>