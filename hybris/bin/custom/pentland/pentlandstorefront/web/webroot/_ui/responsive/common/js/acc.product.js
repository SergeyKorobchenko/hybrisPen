ACC.product = {

    _autoload: [
        "bindToAddToCartForm",
        "enableStorePickupButton",
        "enableVariantSelectors",
        "bindFacets",
        "bindExportCsvClick",
        "bindExportImagesClick"
    ],


    bindFacets: function () {
        $(document).on("click", ".js-show-facets", function (e) {
            e.preventDefault();
            var selectRefinementsTitle = $(this).data("selectRefinementsTitle");
            ACC.colorbox.open(selectRefinementsTitle, {
                href: ".js-product-facet",
                inline: true,
                width: "480px",
                onComplete: function () {
                    $(document).on("click", ".js-product-facet .js-facet-name", function (e) {
                        e.preventDefault();
                        $(".js-product-facet  .js-facet").removeClass("active");
                        $(this).parents(".js-facet").addClass("active");
                        $.colorbox.resize()
                    })
                },
                onClosed: function () {
                    $(document).off("click", ".js-product-facet .js-facet-name");
                }
            });
        });
        enquire.register("screen and (min-width:" + screenSmMax + ")", function () {
            $("#cboxClose").click();
        });
    },


    enableAddToCartButton: function () {
        $('.js-enable-btn').each(function () {
            if (!($(this).hasClass('outOfStock') || $(this).hasClass('out-of-stock'))) {
                $(this).prop("disabled", false);
            }
        });
        $('#addToCartButton').on("click", function(e){
            if ($(".js-total-items-count").length != 0 && $(".js-total-items-count").text() > 0) {
                e.preventDefault();
                e.stopImmediatePropagation();
                $("#addToCartBtn").click();
            }
        });
    },

    enableVariantSelectors: function () {
        $('.variant-select').prop("disabled", false);
    },

    bindToAddToCartForm: function () {
        var addToCartForm = $('.add_to_cart_form');
        addToCartForm.ajaxForm({
        	beforeSubmit:ACC.product.showRequest,
        	success: ACC.product.displayAddToCartPopup
         });    
        setTimeout(function(){
        	$ajaxCallEvent  = true;
         }, 2000);
     },
     showRequest: function(arr, $form, options) {  
    	 if($ajaxCallEvent)
    		{
    		 $ajaxCallEvent = false;
    		 return true;
    		}   	
    	 return false;
 
    },

    bindToAddToCartStorePickUpForm: function () {
        var addToCartStorePickUpForm = $('#colorbox #add_to_cart_storepickup_form');
        addToCartStorePickUpForm.ajaxForm({success: ACC.product.displayAddToCartPopup});
    },

    enableStorePickupButton: function () {
        $('.js-pickup-in-store-button').prop("disabled", false);
    },

    displayAddToCartPopup: function (cartResult, statusText, xhr, formElement) {
    	$ajaxCallEvent=true;
        $('#addToCartLayer').remove();
        if (typeof ACC.minicart.updateMiniCartDisplay == 'function') {
            ACC.minicart.updateMiniCartDisplay();
        }
        var titleHeader = $('#addToCartTitle').html();

        ACC.colorbox.open(titleHeader, {
            html: cartResult.addToCartLayer,
            width: "460px"
        });

        var productCode = $('[name=productCodePost]', formElement).val();
        var quantityField = $('[name=qty]', formElement).val();

        var quantity = 1;
        if (quantityField != undefined) {
            quantity = quantityField;
        }

        var cartAnalyticsData = cartResult.cartAnalyticsData;

        var cartData = {
            "cartCode": cartAnalyticsData.cartCode,
            "productCode": productCode, "quantity": quantity,
            "productPrice": cartAnalyticsData.productPostPrice,
            "productName": cartAnalyticsData.productName
        };
        ACC.track.trackAddToCart(productCode, quantity, cartData);
    },

    bindExportCsvClick: function(){
        $(".exportCsvFromPLP").on("click", function(e){
            e.preventDefault();
        var productsOnPage = [];
        $(".product-item").each(function(){
            var product = {
                "stylecode": $(this).find("input[name='stylecode']").val(),
                "materialKey": $(this).find("input[name='materialKey']").val(),
                "sku": $(this).find("input[name='ean']").val(),
                "name": $(this).find(".name").text().trim(),
                "upc": $(this).find("input[name='upc']").val(),
                "price": $(this).find("input[name='price']").val().trim()
            };
            productsOnPage.push(product);
        });
            var $form = $("#export-csv");
           // $form.find("input[name='content']").val(JSON.stringify(productsOnPage));
            $form.submit();
        });
    },

    bindExportImagesClick: function(){
        $(".exportImagesFromPLP").on("click", function(e){
            e.preventDefault();
            var productsOnPage = [];
            $(".product-item").each(function(){
                var product = $(this).find("input[name='ean']").val();
                productsOnPage.push(product);
            });
            var $form = $("#export-images");
            //$form.find("input[name='content']").val(productsOnPage);
            $form.submit();
        });
    }
};

$(document).ready(function () {
    $ajaxCallEvent = true;
    ACC.product.enableAddToCartButton();
    $(".js-export-block").removeClass("hidden");
});