import regExp from "@/utils/regExp";
import moment from "moment";

const filters = {
    fmtFloat(value) {
        return value.toFixed(2);
    },
    fmtMoney(value) {
        if (value) {
            return value.toFixed(2).replace(regExp.replace.money, ",");
        }
        return value;
    },
    fmtDate(date) {
        if (date) {
            return moment(date).format("YYYY-MM-DD");
        }
    }
};

// 导出filters对象
export default filters;
