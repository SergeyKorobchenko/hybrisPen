ACC.productDetail = {

    _autoload: [
        "initPageEvents",
        "bindVariantOptions"
    ],


    checkQtySelector: function (self, mode) {
        var input = $(self).parents(".js-qty-selector").find(".js-qty-selector-input");
        var inputVal = parseInt(input.val());
        var max = input.data("max");
        var minusBtn = $(self).parents(".js-qty-selector").find(".js-qty-selector-minus");
        var plusBtn = $(self).parents(".js-qty-selector").find(".js-qty-selector-plus");

        $(self).parents(".js-qty-selector").find(".btn").removeAttr("disabled");

        if (mode == "minus") {
            if (inputVal != 1) {
                ACC.productDetail.updateQtyValue(self, inputVal - 1)
                if (inputVal - 1 == 1) {
                    minusBtn.attr("disabled", "disabled")
                }

            } else {
                minusBtn.attr("disabled", "disabled")
            }
        } else if (mode == "reset") {
            ACC.productDetail.updateQtyValue(self, 1)

        } else if (mode == "plus") {
            if(max == "FORCE_IN_STOCK") {
                ACC.productDetail.updateQtyValue(self, inputVal + 1)
            } else if (inputVal <= max) {
                ACC.productDetail.updateQtyValue(self, inputVal + 1)
                if (inputVal + 1 == max) {
                    plusBtn.attr("disabled", "disabled")
                }
            } else {
                plusBtn.attr("disabled", "disabled")
            }
        } else if (mode == "input") {
            if (inputVal == 1) {
                minusBtn.attr("disabled", "disabled")
            } else if(max == "FORCE_IN_STOCK" && inputVal > 0) {
                ACC.productDetail.updateQtyValue(self, inputVal)
            } else if (inputVal == max) {
                plusBtn.attr("disabled", "disabled")
            } else if (inputVal < 1) {
                ACC.productDetail.updateQtyValue(self, 1)
                minusBtn.attr("disabled", "disabled")
            } else if (inputVal > max) {
                ACC.productDetail.updateQtyValue(self, max)
                plusBtn.attr("disabled", "disabled")
            }
        } else if (mode == "focusout") {
            if (isNaN(inputVal)){
                ACC.productDetail.updateQtyValue(self, 1);
                minusBtn.attr("disabled", "disabled");
            } else if(inputVal >= max) {
                plusBtn.attr("disabled", "disabled");
            }
        }

    },

    updateQtyValue: function (self, value) {
        var input = $(self).parents(".js-qty-selector").find(".js-qty-selector-input");
        var addtocartQty = $(self).parents(".addtocart-component").find("#addToCartForm").find(".js-qty-selector-input");
        var configureQty = $(self).parents(".addtocart-component").find("#configureForm").find(".js-qty-selector-input");
        input.val(value);
        addtocartQty.val(value);
        configureQty.val(value);
    },

    showHideEditableGrid: function (element, event) {
        $("#productShowGridAction").animate({
            'height': 'toggle',
        }, 1000);
        $(element).find(".glyphicon").toggleClass('glyphicon-chevron-down');
        $(element).find(".glyphicon").toggleClass('glyphicon-chevron-up');
    },

    populateAndShowEditableGrid: function (element, event) {
        if ($(element).hasClass('open')) {
            if ($('.product-grid-container').children().length > 0) {
                $(window).scrollTo($('#AddToCartOrderForm'), 1000);
            }
        } else {
            var $grid = $("#ajaxGrid");
            var $gridEntry = $('#grid');

            var $orderForm = $('.js-product-order-form');

            $(element).toggleClass('open');

            var targetUrl = $gridEntry.data("target-url");
            var productCode = $gridEntry.data("product-code");

            var method = "GET";
            $.ajax({
                url: targetUrl,
                data: {productCode: productCode},
                type: method,
                success: function (data) {
                    var $grid = $("#ajaxGrid");
                    var $gridEntry = $('#grid');
                    $grid.html(data);
                    $grid.removeAttr('id');
                    $grid.slideDown("fast");
                    $orderForm.slideDown("slow");
                    $(window).scrollTo($('#AddToCartOrderForm'), 1000);
                    setTimeout(function()
                    {
                        ACC.productorderform.coreTableActions();
                    }, 500);
                },
                error: function (xht, textStatus, ex) {
                    alert("Failed to get variant matrix. Error details [" + xht + ", " + textStatus + ", " + ex + "]");
                }

            });
        }
    },

    initPageEvents: function () {

        $( function() {
            $( "#tabs" ).tabs();
        } );

        $( function() {
            $.colorbox.remove();
        } );

        $(document).on("click", '.js-show-editable-product-grid', function (event) {
            ACC.productDetail.populateAndShowEditableGrid(this, event);
        });

        $(document).on("click", '.js-show-editable-product-form-grid', function (event) {
            ACC.productDetail.showHideEditableGrid(this, event);
        });

        $(document).on("click", '.js-qty-selector .js-qty-selector-minus', function () {
            ACC.productDetail.checkQtySelector(this, "minus");
        })

        $(document).on("click", '.js-qty-selector .js-qty-selector-plus', function () {
            ACC.productDetail.checkQtySelector(this, "plus");
        })

        $(document).on("keydown", '.js-qty-selector .js-qty-selector-input', function (e) {

            if (($(this).val() != " " && ((e.which >= 48 && e.which <= 57 ) || (e.which >= 96 && e.which <= 105 ))  ) || e.which == 8 || e.which == 46 || e.which == 37 || e.which == 39 || e.which == 9) {
            }
            else if (e.which == 38) {
                ACC.productDetail.checkQtySelector(this, "plus");
            }
            else if (e.which == 40) {
                ACC.productDetail.checkQtySelector(this, "minus");
            }
            else {
                e.preventDefault();
            }
        })

        $(document).on("keyup", '.js-qty-selector .js-qty-selector-input', function (e) {
            ACC.productDetail.checkQtySelector(this, "input");
            ACC.productDetail.updateQtyValue(this, $(this).val());

        })

        $(document).on("focusout", '.js-qty-selector .js-qty-selector-input', function (e) {
            ACC.productDetail.checkQtySelector(this, "focusout");
            ACC.productDetail.updateQtyValue(this, $(this).val());
        })

        $("#Size").change(function () {
            changeOnVariantOptionSelection($("#Size option:selected"));
        });

        $("#variant").change(function () {
            changeOnVariantOptionSelection($("#variant option:selected"));
        });

        $(".selectPriority").change(function () {
            window.location.href = $(this[this.selectedIndex]).val();
        });

        function changeOnVariantOptionSelection(optionSelected) {
            window.location.href = optionSelected.attr('value');
        }
    },

    bindVariantOptions: function () {
        ACC.productDetail.bindCurrentStyle();
        ACC.productDetail.bindCurrentSize();
        ACC.productDetail.bindCurrentType();
    },

    bindCurrentStyle: function () {
        var currentStyle = $("#currentStyleValue").data("styleValue");
        var styleSpan = $(".styleName");
        if (currentStyle != null) {
            styleSpan.text(": " + currentStyle);
        }
    },

    bindCurrentSize: function () {
        var currentSize = $("#currentSizeValue").data("sizeValue");
        var sizeSpan = $(".sizeName");
        if (currentSize != null) {
            sizeSpan.text(": " + currentSize);
        }
    },

    bindCurrentType: function () {
        var currentSize = $("#currentTypeValue").data("typeValue");
        var sizeSpan = $(".typeName");
        if (currentSize != null) {
            sizeSpan.text(": " + currentSize);
        }
    }
};