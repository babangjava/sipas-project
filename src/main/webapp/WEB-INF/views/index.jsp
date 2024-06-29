<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/util" %>
<%@ taglib prefix="display" tagdir="/WEB-INF/tags/display" %>

<div id="page-inner">

        <!-- /. ROW  -->

        <div class="row">
                <div class="col-md-3 col-sm-12 col-xs-12">
                        <div class="panel panel-primary text-center no-boder blue">
                                <div class="panel-left pull-left blue">
                                        <i class="glyphicon glyphicon-import fa-5x"></i>

                                </div>
                                <div class="panel-right">
                                        <h3>${totalSuratMasuk}</h3>
                                        <strong> Surat Masuk</strong>
                                </div>
                        </div>
                </div>
                <div class="col-md-3 col-sm-12 col-xs-12">
                        <div class="panel panel-primary text-center no-boder blue">
                                <div class="panel-left pull-left blue">
                                        <i class="glyphicon glyphicon-export fa-5x"></i>
                                </div>

                                <div class="panel-right">
                                        <h3>${totalSuratKeluar}</h3>
                                        <strong> Surat Keluar</strong>

                                </div>
                        </div>
                </div>
                <div class="col-md-3 col-sm-12 col-xs-12">
                        <div class="panel panel-primary text-center no-boder blue">
                                <div class="panel-left pull-left blue">
                                        <i class="glyphicon glyphicon-book fa-5x"></i>

                                </div>
                                <div class="panel-right" style="width: 200px">
                                        <h3>${totalDocumentUpload}</h3>
                                        <strong> PPD</strong>

                                </div>
                        </div>
                </div>
                <div class="col-md-3 col-sm-12 col-xs-12">
                        <div class="panel panel-primary text-center no-boder blue">
                                <div class="panel-left pull-left blue">
                                        <i class="fa fa-users fa-5x"></i>

                                </div>
                                <div class="panel-right">
                                        <h3>${totalUser}</h3>
                                        <strong> User Account</strong>

                                </div>
                        </div>
                </div>
        </div>

        <div class="row">
                <div class="col-md-6 col-sm-12 col-xs-12">

                        <div class="panel panel-default">
                                <div class="panel-heading">
                                        Surat Masuk Minggu Ini
                                </div>
                                <div class="panel-body">
                                        <div class="table-responsive">
                                                <table class="table table-striped table-bordered table-hover">
                                                        <thead>
                                                        <tr>
                                                                <th>Nomor Surat</th>
                                                                <th>Tanggal</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <c:forEach items="${suratMasukHariIniList}" var="item" >
                                                                <tr>
                                                                        <td><a target="_blank" href="${pageContext.request.contextPath}/suratmasuk/download/${item.documentId}">${item.noSurat}</a></td>
                                                                        <td><display:date value="${item.tanggal}"/></td>
                                                                </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                </table>
                                        </div>
                                </div>
                        </div>

                </div>
                <div class="col-md-6 col-sm-12 col-xs-12">

                        <div class="panel panel-default">
                                <div class="panel-heading">
                                        Surat Keluar Minggu Ini
                                </div>
                                <div class="panel-body">
                                        <div class="table-responsive">
                                                <table class="table table-striped table-bordered table-hover">
                                                        <thead>
                                                        <tr>
                                                                <th>Nomor Surat</th>
                                                                <th>Tanggal</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <c:forEach items="${suratKeluarHariIniList}" var="item">
                                                                <tr>
                                                                        <td><a target="_blank" href="${pageContext.request.contextPath}/suratkeluar/download/${item.documentId}">${item.noSurat}</a></td>
                                                                        <td><display:date value="${item.tanggal}"/></td>
                                                                </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                </table>
                                        </div>
                                </div>
                        </div>

                </div>
        </div>

</div>