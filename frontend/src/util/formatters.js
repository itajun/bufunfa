import { format } from 'date-fns';
import numeral from 'numeral';

export const localDate = (value) => {
    let date = value;
    if (typeof value === 'string') {
        date = new Date(value);
    }
    try {
        return format(date, 'dd/MM/yyyy');
    } catch (e) {
        return "N/A";
    }
}

export const localDateTime = (value) => {
    let date = value;
    if (typeof value === 'string') {
        date = new Date(value);
    }
    try {
        return format(date, 'dd/MM/yyyy HH:mm');
    } catch (e) {
        return 'N/A';
    }
}

export const graphqlDate = (value) => {
    if (!value) return null
    let date = value;
    if (typeof value === 'string') {
        date = new Date(value);
    }
    return format(date, 'yyyy-MM-dd');
}

export const graphqlDateTime = (value) => {
    let date = value;
    if (typeof value === 'string') {
        date = new Date(value);
    }
    return date.toISOString();
}

export const centToCurr = (value) => {
    return numeral(value).divide(100).format('0,0.00');
}

export const floatToCents = (value) => {
    let float = value;
    if (typeof value === 'string') {
        value = value.replace(',', '')
        float = parseFloat(value);
    }
    return Math.trunc(float * 100);
}
