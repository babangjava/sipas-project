<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="container" style="margin-top: 5%;">
    <div class="col-md-4 col-md-offset-4">
        <div class="panel panel-primary">
            <div class="panel-heading">Login</div>
            <div class="panel-body">
                <form id="loginform" name="loginform" class="form-vertical" action="${pageContext.request.contextPath}/authenticate"  method="POST">
                    <%--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />--%>
                    <!-- Username Field -->
                    <c:if test="${requestScope['javax.servlet.forward.query_string']=='error'}">
                        <div id="error" class="alert alert-danger" style="text-align: center">Username and Password not match</div>
                    </c:if>
                    <div class="row">
                        <div class="form-group col-xs-12">
                            <label for="username"><span class="text-danger" style="margin-right:5px;">*</span>Username:</label>

                            <div class="input-group">
                                <input class="form-control" id="username" type="text" name="username"
                                       placeholder="Username" required/>
                            <span class="input-group-btn">
                                <label class="btn btn-primary"><span class="glyphicon glyphicon-user" aria-hidden="true"></label>
                            </span>
                                </span>
                            </div>
                        </div>
                    </div>

                    <!-- Content Field -->
                    <div class="row">
                        <div class="form-group col-xs-12">
                            <label for="password"><span class="text-danger" style="margin-right:5px;">*</span>Password:</label>

                            <div class="input-group">
                                <input class="form-control" id="password" type="password" name="password"
                                       placeholder="Password" required/>
                            <span class="input-group-btn">
                                <label class="btn btn-primary"><span class="glyphicon glyphicon-lock" aria-hidden="true"></label>
                            </span>
                            </span>
                            </div>
                        </div>
                    </div>
                    <!-- Login Button -->
                    <div class="row">
                        <div class="form-group col-xs-4">
                            <button class="btn btn-primary" type="submit">Submit</button>
                        </div>
                    </div>
                    <script type="text/javascript">
//                        window.onload = function() {
//                            var origin   = window.location.href;
//                            if(!origin.includes('https')){
//                                window.location.href = 'https://sipas-project.herokuapp.com/login';
//                            }
//                        }
                    </script>
                </form>
            </div>
        </div>
    </div>
</div>
