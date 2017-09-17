ACC.internationalization = {
    //translates a single message for key (to be used in javascript)
    //arguments are separated by ','
    translateMessage: function(messageKey, arguments) {
        var requestUrl = ACC.config.encodedContextPath + "/translateMessage";
        var jsonData = {};
        jsonData['key'] = messageKey;
        if(arguments != undefined) {
            jsonData['arguments'] = arguments;
        } else {
            jsonData['arguments'] = '';
        }
        var translation = messageKey;
        $.ajax({
            url: requestUrl,
            data: jsonData,
            dataType: 'json',
            type: 'POST',
            async: false,
            success: function(data) {
                translation = data;
            },
            error: function(error) {
                console.log(error)
            }
        });
        return translation;
    },
    //parses all messages on the page for ajaxTheme tag
    parseLocalizedMessages: function() {

        //collect data
        var jsonData = {};
        $(".ajax_message").each(function(index, message){
            jsonData[index] = [$(message).data('code'),$(message).data('arguments'), $(message).data('unit'), $(message).data('text_only')];
        });

        var requestUrl = ACC.config.encodedContextPath + "/localize";

        //query translations
        $.ajax({
            url: requestUrl,
            data: jsonData,
            dataType: 'json',
            type: 'POST',
            success: function(data) {
                $(".ajax_message").each(function(index, message){
                    var text = data[index+"[]"];
                    $(message).html(text);
                });
                $(".ajax_unit_message").each(function(index, message){
                    var text = data[index+"[]"];
                    $(message).html(text);
                });
            }
        });
    }
};
$(document).ready(function(){
    //skip https pages for performance
    if(window.location.protocol.indexOf("https") == -1){
        ACC.internationalization.parseLocalizedMessages();
    }
})