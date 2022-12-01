import regExp from "@/utils/regExp";

const filters = {
    fmtFloat(value) {
        return value.toFixed(2);
    },
    fmtMoney(value) {
        if (value) {
            return value.toFixed(2).replace(regExp.replace.money, ",");
        }
        return value;
    }
};

// 导出filters对象
export default filters;
