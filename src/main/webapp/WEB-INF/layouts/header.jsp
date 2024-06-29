<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div id="wrapper" style="overflow-x: hidden; overflow-y: hidden">
    <nav class="navbar navbar-default top-navbar" role="navigation">
        <div class="navbar-header" style="pointer-events: none;">
            <div class="navbar-brand" style="width: 500px" href="/"><strong>PT WIKA TIRTA JAYA JATILUHUR</strong></div>
        </div>

        <ul class="nav navbar-top-links navbar-right">
            <!-- /.dropdown -->
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false">
                    <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
                </a>
                <ul class="dropdown-menu dropdown-user">
                    <li>
                        <a href="${pageContext.request.contextPath}/change/password"><i class="fa fa-gear fa-fw"></i> Change Password</a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <form action="${pageContext.request.contextPath}/logout" method="post" id="logoutForm">
                            <%--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />--%>
                        </form>
                        <script>
                            function formSubmit() {
                                document.getElementById("logoutForm").submit();
                            }
                        </script>
                        <a href="javascript:formSubmit()"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                    </li>

                </ul>
                <!-- /.dropdown-user -->
            </li>
            <!-- /.dropdown -->
        </ul>
    </nav>
    <!--/. NAV TOP  -->
    <c:if test="${role == 'ROLE_ADMIN'}">
    <nav class="navbar-default navbar-side" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav" id="main-menu">
                <li>
                    <a class="active-menu" href="${pageContext.request.contextPath}/index"><i class="fa fa-dashboard"></i> Dashboard</a>
                </li>
                <li>
                    <a href="#"><i class="glyphicon glyphicon-chevron-down"></i> Master</a>
                    <ul class="nav nav-second-level collapse">
                        <li><a href="${pageContext.request.contextPath}/user">User</a> </li>
                        <li><a href="${pageContext.request.contextPath}/menuitem">Menu Item</a></li>
                    </ul>
                </li>
                <li>
                    <a href="#"><i class="glyphicon glyphicon-chevron-down"></i> Document Surat</a>
                    <ul class="nav nav-second-level collapse">
                        <li><a href="${pageContext.request.contextPath}/suratmasuk">Surat Masuk</a></li>
                        <li><a href="${pageContext.request.contextPath}/suratkeluar">Surat Keluar</a></li>
                    </ul>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/uploaddocument"><i class="glyphicon glyphicon-minus"></i>  PPD</a>
                </li>
                <li>
                    <a href="#"><i class="glyphicon glyphicon-chevron-down"></i> Laporan</a>
                    <ul class="nav nav-second-level collapse">
                        <li><a href="${pageContext.request.contextPath}/report/suratmasuk">Surat Masuk</a></li>
                        <li><a href="${pageContext.request.contextPath}/report/suratkeluar">Surat Keluar</a></li>
                    </ul>
                </li>
            </ul>
        </div>

    </nav>
    </c:if>
    <c:if test="${role == 'ROLE_USER'}">
    <nav class="navbar-default navbar-side" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav" id="main-menu">
                <li>
                    <a class="active-menu" href="${pageContext.request.contextPath}/index"><i class="fa fa-dashboard"></i> Dashboard</a>
                </li>
                <li>
                    <a href="#"><i class="glyphicon glyphicon-chevron-down"></i> Document Surat</a>
                    <ul class="nav nav-second-level collapse">
                        <li><a href="${pageContext.request.contextPath}/suratmasuk">Surat Masuk</a></li>
                        <li><a href="${pageContext.request.contextPath}/suratkeluar">Surat Keluar</a></li>
                    </ul>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/uploaddocument"><i class="glyphicon glyphicon-minus"></i>  PPD</a>
                </li>
            </ul>
        </div>

    </nav>
    </c:if>

    <!-- /. NAV SIDE  -->
    <div id="page-wrapper">

        <div id="page-inner">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div id="pageName">Home</div>
                        </div>
