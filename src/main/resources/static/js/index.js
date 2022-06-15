$(function() {

	//run job once
    $(".btnRun").click(function() {
    	var id = $(this).parent().data("id");
    	console.log(id);
        $.ajax({
            url: "/api/runJob?t=" + new Date().getTime(),
            type: "POST",
            data: {
                "jobName": $("#name_"+id).text(),
                "jobGroup": $("#group_"+id).text()
            },
            success: function(res) {
                if (res.valid) {
                	alert("run success!");  
                } else {
                	alert(res.msg); 
                }
            }
        });
    });
    
    //pause job
    $(".btnPause").click(function() {
    	var id = $(this).parent().data("id");
        $.ajax({
            url: "/api/pauseJob?t=" + new Date().getTime(),
            type: "POST",
            data: {
                "jobName": $("#name_"+id).text(),
                "jobGroup": $("#group_"+id).text()
            },
            success: function(res) {
                if (res.valid) {
                	alert("pause success!");
                	location.reload();
                } else {
                	alert(res.msg); 
                }
            }
        });
    });
    
    //resume job
    $(".btnResume").click(function() {
    	var id = $(this).parent().data("id");
        $.ajax({
            url: "/api/resumeJob?t=" + new Date().getTime(),
            type: "POST",
            data: {
                "jobName": $("#name_"+ id).text(),
                "jobGroup": $("#group_"+id).text()
            },
            success: function(res) {
                if (res.valid) {
                	alert("resume success!");
                	location.reload();
                } else {
                	alert(res.msg); 
                }
            }
        });
    });
    
    //delete job
    $(".btnDelete").click(function() {
    	var id = $(this).parent().data("id");
        $.ajax({
            url: "/api/deleteJob?t=" + new Date().getTime(),
            type: "POST",
            data: {
                "jobName": $("#name_"+ id).text(),
                "jobGroup": $("#group_"+ id).text()
            },
            success: function(res) {
                if (res.valid) {
                	alert("delete success!");
                	location.reload();
                } else {
                	alert(res.msg); 
                }
            }
        });
    });
	
	// update cron expression
    $(".btnEdit").click(
    		function() {
    			$("#myModalLabel").html("cron edit");
    			var id = $(this).parent().data("id");
    			$("#id").val(id);
    			$("#edit_name").val($("#name_"+id).text());
    			$("#edit_group").val($("#group_"+id).text());
    			$("#edit_cron").val($("#cron_"+id).text());
    			$("#edit_status").val($("#status_"+id).text());
    			$("#edit_descr").val($("#descr_"+id).text());
    			
    			$('#edit_name').attr("readonly","readonly"); 
    			$('#edit_group').attr("readonly","readonly");
    			$('#edit_descr').attr("readonly","readonly");
    			
    			$("#myModal").modal("show");
    });
    
    $("#save").click(
	    function() {
	    	$.ajax({
	            url: "/api/saveOrUpdate?t=" + new Date().getTime(),
	            type: "POST",
	            data:  $('#mainForm').serialize(),
	            success: function(res) {
	            	if (res.valid) {
	                	alert("success!");
	                	location.reload();
	                } else {
	                	alert(res.msg); 
	                }
	            }
	        });
    });


    // create job
    $("#createBtn").click(
    		function() {
    			$("#myModalLabel").html("Create Job");
    			
    			$("#id").val("");
    			$("#edit_name").val("");
    			$("#edit_group").val("");
    			$("#edit_cron").val("");
    			$("#edit_status").val("NORMAL");
    			$("#edit_descr").val("");
    			
    			$('#edit_name').removeAttr("readonly");
    			$('#edit_group').removeAttr("readonly");
    			$('#edit_descr').removeAttr("readonly");
    			
    			$("#myModal").modal("show");
    });
    
    
});