ACC.loader = {
    loaderClassName: "p-loader",
    show: function() {
        $('.'+this.loaderClassName).show();
    },
    hide: function() {
        $('.'+this.loaderClassName).hide();
    }
};