page.dialogs.elements.frmCreateStaff.validate({
    rules: {
        fullNameCre: {
            required: true,
            minlength: 5,
            maxlength: 25
        },
        phoneCre: {
            required: true,
        },
        userNameCre: {
            required: true,
            isEmail: true
        },
        dobCre: {
            required: true,
        },
        addressCre: {
            required: true
        }

    },
    messages: {
        fullNameCre: {
            required: "Vui lòng nhập họ và tên",
            minlength: 'Họ tên tối thiểu là 5 ký tự',
            maxlength: 'Họ tên tối đa là 25 ký tự'
        },
        phoneCre: {
            required: "Vui lòng nhập số điện thoại",
        },
        userNameCre: {
            required: "Vui lòng nhập tài khoản",
        },
        dobCre: {
            required: "Vui lòng nhập ngày sinh",
        },
        addressCre: {
            required: "Vui lòng nhập địa chỉ "
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
            $("#frmCreateStaff input.error").removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.commands.handleCreateStaff();
    }


})
page.dialogs.elements.frmUpdateStaff.validate({
    rules: {
        fullNameUp: {
            required: true

        },
        phoneUp: {
            required: true,
            isNumberWithSpace: true
        },
        addressUp: {
            required: true,
        }

    },
    messages: {
        fullNameUp: {
            required: "Vui lòng nhập họ và tên"
        },
        phoneUp: {
            required: "Vui lòng nhập số điện thoại",
            isNumberWithSpace: "Vui lòng nhập phone bằng ký tự số"
        },
        addressUp: {
            required: "Vui lòng nhập địa chỉ",
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
            $("#formUpdate input.error").removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.commands.updateStaff();
    }
})
$.validator.addMethod("isEmail", function (value, element) {
    return this.optional(element) || /[A-Za-z0-9+_.-]+@[a-z]+\.[a-z]+/.test(value);
}, "Vui lòng nhập đúng định dạng email");

$.validator.addMethod("isNumberWithSpace", function (value, element) {
    return this.optional(element) || /^\s*[0-9,\s]+\s*$/i.test(value);
}, "Vui lòng nhập giá trị số");