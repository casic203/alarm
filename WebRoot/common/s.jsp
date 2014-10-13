
    <!-- bootstrap -->
    <link type="text/css" rel="stylesheet" href="${ctx}/s/bootstrap/css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="${ctx}/s/bootstrap/css/bootstrap-responsive.min.css">

    <!-- html5 -->
    <!--[if lt IE 9]>
    <link rel="stylesheet" type="text/css" href="${ctx}/s/mossle/css/ie.css" media="screen" />
    <script type="text/javascript" src="${ctx}/s/html5/html5shiv.js"></script>
    <![endif]-->

    <!-- jquery -->
    <script type="text/javascript" src="${ctx}/s/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/s/jquery/jquery-migrate-1.0.0.min.js"></script>
	<!-- bootstrap -->
    <script type="text/javascript" src="${ctx}/s/bootstrap/js/bootstrap.min.js"></script>

    <!-- layout -->
    <script type="text/javascript" src="${ctx}/s/mossle/js/hideshow.js"></script>
    <script type="text/javascript" src="${ctx}/s/mossle/js/jquery.equalHeight.js"></script>
    <script type="text/javascript" src="${ctx}/s/mossle/js/table.js"></script>

    <!-- message -->
    <script type="text/javascript" src="${ctx}/s/jquery-sliding-message/jquery.slidingmessage.min.js"></script>

    <!-- uniform -->
    <link type="text/css" rel="stylesheet" href="${ctx}/s/uniform/css/uniform.default.css" media="screen" />
    <script type="text/javascript" src="${ctx}/s/uniform/js/jquery.uniform.min.js"></script>

    <!-- table and pager -->
    <script type="text/javascript" src="${ctx}/s/pagination/pagination.js"></script>
    <script type="text/javascript" src="${ctx}/s/table/table.js"></script>
    <script type="text/javascript" src="${ctx}/s/table/messages_${locale}.js"></script>

    <!-- validater -->
    <script type="text/javascript" src="${ctx}/s/jquery-validation/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${ctx}/s/jquery-validation/additional-methods.min.js"></script>
    <script type="text/javascript" src="${ctx}/s/jquery-validation/localization/messages_${locale}.js"></script>
    <link type="text/css" rel="stylesheet" href="${ctx}/s/jquery-validation/jquery.validate.css" />

    <!-- datepicker -->
    <link type="text/css" rel="stylesheet" href="${ctx}/s/bootstrap-datepicker/datepicker.css">
    <script type="text/javascript" src="${ctx}/s/bootstrap-datepicker/bootstrap-datepicker.js"></script>

	<!-- uniform -->
    <link rel="stylesheet" href="${ctx}/s/uniform/css/uniform.default.min.css" type="text/css" media="screen" />
    <script type="text/javascript" src="${ctx}/s/uniform/js/jquery.uniform.min.js"></script>

	<!-- chozen -->
    <link rel="stylesheet" href="${ctx}/s/chosen/chosen.css" type="text/css" media="screen" />
    <script type="text/javascript" src="${ctx}/s/chosen/chosen.jquery.min.js"></script>

	<!-- bootbox -->
    <script type="text/javascript" src="${ctx}/s/bootbox/bootbox.min.js"></script>

    <!-- ckeditor -->
    <script type="text/javascript" src="${ctx}/s/ckeditor/ckeditor.js"></script>
    <script type="text/javascript" src="${ctx}/s/ckfinder/ckfinder.js"></script>

    <script type="text/javascript" src="${ctx}/s/jquery-tablednd/jquery.tablednd.min.js"></script>
	<!-- layout -->
    <link type="text/css" rel="stylesheet" href="${ctx}/s/mossle/css/layout2.css" media="screen" />

    <script type="text/javascript">
$(function() {
    $('.m-column').equalHeight(window.innerHeight - 55 - 38);

	$("input:checkbox, input:radio, input:file").not('[data-no-uniform="true"],#uniform-is-ajax').uniform({
		fileDefaultHtml: '还未选择文件',
		fileButtonHtml: '选择文件'
	});

	$('.datepicker').datepicker({format:'yyyy-mm-dd'});

	$(".chzn-select").chosen();
	$(".chzn-select-deselect").chosen({allow_single_deselect:true});

    $.showMessage($('#m-success-message').html(), {
         position: 'top',
         size: '55',
         fontSize: '20px'
    });
});
    </script>
