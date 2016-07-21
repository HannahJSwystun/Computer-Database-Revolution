var list = (function() {
    var initHasRun = false;
    var $listPage = $("#list-page");
    var $pdfReportButton = $listPage.find("#pdf-report-button");
    var $computerCode = $listPage.find(".computer-code");
    var $fileModal = $listPage.find("#fileModal");
    var $fileContainer = $fileModal.find("#file-container");
    var $fileContainerFrame = $fileModal.find("#file-container-frame");

    function init() {
        if (initHasRun) return;

        initHasRun = true;
    }

    $pdfReportButton.click(function() {
        var checkedComputerCodes = [];
        $computerCode.each(function() {
            if ($(this).is(":checked")) {
                checkedComputerCodes.push($(this).val());
            }
        });

        var computerCodes = "";
        for (var i = 0; i < checkedComputerCodes.length; i++) {
            if (i != 0) {
                computerCodes += "&";
            }
            computerCodes += "computercode=" + checkedComputerCodes[i];
        }

        var $newIframe = $fileContainerFrame.clone();
        $fileContainer.empty().append($newIframe);
        $newIframe.attr('src', '/ComputersCustomActionsGet?' + computerCodes + "&action=" + encodeURIComponent("PDF Report"));
        $fileModal.modal('show');
    });

    // public interfaces
    return {
        init: init
    }
}());