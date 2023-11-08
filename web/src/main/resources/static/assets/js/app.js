class App {
    static DOMAIN_SERVER = window.origin;
    static API_SERVER = this.DOMAIN_SERVER + '/api';

    static API_PRODUCT = this.API_SERVER + '/products';
    static API_PRODUCT_SEARCH_NAME = this.API_PRODUCT + '/searchName';
    static API_CREATE_PRODUCT = this.API_PRODUCT
    static API_UPDATE_PRODUCT = this.API_PRODUCT + '/edit'
    static API_DELETE_PRODUCT = this.API_PRODUCT

    static API_TABLE_ORDER = this.API_SERVER + '/tableOrders';

    static API_ORDER = this.API_SERVER + '/orders';
    static API_ORDER_BY_TABLE_ID = this.API_ORDER + '/table';

    static API_CATEGORY = this.API_SERVER + '/categories'

    static API_BILL = this.API_SERVER + '/bills';

    static API_AUTH = this.API_SERVER + "/auth";
    static API_LOGIN = this.API_AUTH + "/login";
    static API_REGISTER = this.API_AUTH + "/register";

    static API_USERS = this.API_SERVER + '/users'
    static API_UPDATE_USER = this.API_USERS + '/update'
    static API_ROLE = this.API_USERS + '/roles'

    static API_STAFF = this.API_SERVER + '/staff'

    static API_REPORT = this.API_SERVER + '/report'

    static BASE_URL_CLOUD_IMAGE = "https://res.cloudinary.com/dadtniwa8/image/upload";
    static IMAGE_SCALE_W_280_h_180_Q_100 = 'c_scale,w_280,h_180,q_100'
    static IMAGE_SCALE_W_250_h_200_Q_90 = 'c_scale,w_250,h_2000,q_90'
    static IMAGE_SCALE_W_100_h_80_Q_90 = 'c_scale,w_100,h_80,q_90'
    static BASE_SCALE_IMAGE = "c_limit,w_50,h_50,q_100";



    static AlertMessageEn = class {
        static SUCCESS_CREATED = "Successful data generation !";
        static SUCCESS_UPDATED = "Data update successful !";
        static SUCCESS_DELETED = "Delete product successful !";
        static SUCCESS_PAYMENT = "Đã thanh toán thành công!"


        static ERROR_400 = "The operation failed, please check the data again.";
        static ERROR_401 = "Unauthorized - Your access token has expired or is not valid.";
        static ERROR_403 = "Forbidden - You are not authorized to access this resource.";
        static ERROR_404 = "Not Found - The resource has been removed or does not exist";
        static ERROR_500 = "Internal Server Error - The server system is having problems or cannot be accessed.";

        static ERROR_LOADING_PROVINCE = "Loading list of provinces - cities failed !";
        static ERROR_LOADING_DISTRICT = "Loading list of district - ward failed !"
        static ERROR_LOADING_WARD = "Loading list of wards - communes - towns failed !";
        static ERROR_NUMBER_QUANTITY = "Số lượng mua vượt quá số lượng đang có!"
        static ERROR_FIND_PRODUCT = "Không tìm thấy sản phẩm!"
    }

    static AlertMessageVi = class {
        static ERROR_400 = "Thao tác không thành công, vui lòng kiểm tra lại dữ liệu.";
        static ERROR_401 = "Unauthorized - Access Token của bạn hết hạn hoặc không hợp lệ.";
        static ERROR_403 = "Forbidden - Bạn không được quyền truy cập tài nguyên này.";
        static ERROR_404 = "Not Found - Tài nguyên này đã bị xóa hoặc không tồn tại";
        static ERROR_500 = "Internal Server Error - Hệ thống Server đang có vấn đề hoặc không truy cập được.";

        static ERROR_LOADING_PROVINCE = "Tải danh sách tỉnh - thành phố không thành công !";
        static ERROR_LOADING_DISTRICT = "Tải danh sách quận - phường - huyện không thành công !";
        static ERROR_LOADING_WARD = "Tải danh sách phường - xã - thị trấn không thành công !";
    }



    static showDeleteConfirmDialog() {
        return Swal.fire({
            icon: 'warning',
            text: 'Are you sure you want to delete the selected data ?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it !',
            cancelButtonText: 'Cancel',
        });
    }

    static showSuccessAlert(t) {
        Swal.fire({
            icon: 'success',
            title: t,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1500,
        });
    }

    static showErrorAlert(t) {
        Swal.fire({
            icon: 'error',
            title: 'Warning',
            text: t,
        });
    }
static showSuccess(m) {
    iziToast.success({
        title: 'OK',
        message: m,
    });
}
    static IziToast = class {
        static showSuccessAlert(m) {
            iziToast.success({
                title: 'OK',
                position: 'bottomRight',
                timeout: 2500,
                message: m
            });
        }

        static showErrorAlert(m) {
            iziToast.error({
                title: 'Error',
                position: 'topRight',
                timeout: 2500,
                message: m
            });
        }
    }

    static SweetAlert = class {
        static showDeleteConfirmDialog() {
            return Swal.fire({
                icon: 'warning',
                text: 'Bạn có muốn khóa tài khoản này không',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Chắc chắn',
                cancelButtonText: 'Huỷ',
            })
        }

        static showSuccessAlert(t) {
            Swal.fire({
                icon: 'success',
                title: t,
                position: 'center',
                showConfirmButton: false,
                timer: 1500
            })
        }

        static showErrorAlert(t) {
            Swal.fire({
                icon: 'error',
                title: 'Warning',
                text: t,
            })
        }

        static showError401() {
            Swal.fire({
                icon: 'error',
                title: 'Access Denied',
                text: 'Invalid credentials!',
            })
        }

        static showError403() {
            Swal.fire({
                icon: 'error',
                title: 'Access Denied',
                text: 'You are not authorized to perform this function!',
            })
        }
    }
}

class Product{
    constructor(id,title,price,unit,category,avatar) {
        this.id=id;
        this.title=title;
        this.price=price;
        this.unit=unit;
        this.category=category;
        this.avatar=avatar;
    }
}
class TableOrder{
    constructor(id,title,status) {
        this.id=id;
        this.title=title;
        this.status =status;
    }
}

class Category {
    constructor(id,title) {
        this.id = id;
        this.title = title;
    }
}
class Unit {
    constructor(id,title) {
        this.id = id;
        this.title = title;
    }
}
class Zone {
    constructor(id,title) {
        this.id = id;
        this.title = title;
    }
}


class Avatar {
    constructor(fileId, fileName, fileFolder, fileUrl) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileFolder = fileFolder;
        this.fileUrl = fileUrl;
    }
}

class LocationRegion {
    constructor(id, provinceId, provinceName, districtId, districtName, wardId, wardName, address) {
        this.id = id;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.wardId = wardId;
        this.wardName = wardName;
        this.address = address;
    }
}

class Staff {
    constructor(id,title,phone,locationRegion,staffAvatar) {
        this.id=id;
        this.title=title;
        this.phone=phone;
        this.locationRegion=locationRegion;
        this.staffAvatar=staffAvatar;

    }
}

class User {
    constructor(id,userName,passWord,role) {
        this.id =id;
        this.userName=userName;
        this.passWord=passWord;
        this.role=role;

    }
}


// $(function() {
//     $(".num-space").number(true, 0, '', ' ');
//     $(".num-point").number(true, 0, '', '.');
//     $(".num-comma").number(true, 0, '', ',');
//
//     $('[data-toggle="tooltip"]').tooltip();
// });
