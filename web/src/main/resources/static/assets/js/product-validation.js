page.dialogs.elements.frmCreateProduct.validate({
    rules: {
        titleCre: {
            required: true,
        },
        priceCre: {
            required: true,
            min: 100,
            max: 999999999,
            number: true
        },
        unitCre: {
            required: true
        },
        categoryCre: {
            required: true,
            number: true

        }
    },
    messages: {
        titleCre: {
            required: "Vui lòng nhập tên sản phẩm.",
        },
        priceCre: {
            required: "Vui lòng nhập giá.",
            number: "Giá sản phẩm phải là số.",
            min: "Giá sản phẩm tối thiểu là 100 VNĐ.",
            max: "Giá sản phẩm tối đa là 999.999.999 VNĐ."
        },
        unitCre: {
            required: "Vui lòng nhập loại sản phẩm."
        },
        categoryCre: {
            required: "Vui lòng nhập loại sản phẩm.",
            number: "Mã danh mục không đúng. Vui lòng chọn lại."
        }
    },
    errorLabelContainer: "#modalCreate .modal-alert-danger",
    errorPlacement: function (error, element) {
        error.appendTo("#modalCreate .modal-alert-danger");
    },
    showErrors: function (errorMap, errorList) {
        if (this.numberOfInvalids() > 0) {
            $("#modalCreate .modal-alert-danger").removeClass("hide").addClass("show");
        } else {
            $("#modalCreate .modal-alert-danger").removeClass("show").addClass("hide").empty();
            $("#frmCreateProduct input.error").removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.commands.createProduct();
    }
})
page.dialogs.elements.frmUpdateProduct.validate({
    rules: {
        titleUp: {
            required: true,
        },
        priceUp: {
            required: true,
            min: 100,
            max: 999999999,
            number: true
        },
        unitUp: {
            required: true
        },
        categoryUp: {
            required: true,
            number: true

        }
    },
    messages: {
        titleUp: {
            required: "Vui lòng nhập tên sản phẩm.",
        },
        priceUp: {
            required: "Vui lòng nhập giá.",
            number: "Giá sản phẩm phải là số.",
            min: "Giá sản phẩm tối thiểu là 100 VNĐ.",
            max: "Giá sản phẩm tối đa là 999.999.999 VNĐ."
        },

        unitUp: {
            required: "Vui lòng nhập loại sản phẩm."
        },
        categoryUp: {
            required: "Vui lòng nhập loại sản phẩm.",
            number: "Mã danh mục không đúng. Vui lòng chọn lại."
        }
    },
    errorLabelContainer: "#modalUpdate .modal-alert-danger",
    errorPlacement: function (error, element) {
        error.appendTo("#modalUpdate .modal-alert-danger");
    },
    showErrors: function (errorMap, errorList) {
        if (this.numberOfInvalids() > 0) {
            $("#modalUpdate .modal-alert-danger").removeClass("hide").addClass("show");
        } else {
            $("#modalUpdate .modal-alert-danger").removeClass("show").addClass("hide").empty();
            $("#frmUpdateProduct input.error").removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.dialogs.commands.updateProduct();
    }
})