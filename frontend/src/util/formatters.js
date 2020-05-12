import { format } from "date-fns";
import numeral from "numeral";

export const localDate = (value) => {
  let date = value;
  if (typeof value === "string") {
    date = new Date(value);
  }
  try {
    return format(date, "dd/MM/yyyy");
  } catch (e) {
    return "N/A";
  }
};

export const localDateTime = (value) => {
  let date = value;
  if (typeof value === "string") {
    date = new Date(value);
  }
  try {
    return format(date, "dd/MM/yyyy HH:mm");
  } catch (e) {
    return "N/A";
  }
};

export const graphqlDate = (value) => {
  if (!value) return null;
  let date = value;
  if (typeof value === "string") {
    date = new Date(value);
  }
  return format(date, "yyyy-MM-dd");
};

export const graphqlDateTime = (value) => {
  let date = value;
  if (typeof value === "string") {
    date = new Date(value);
  }
  return date.toISOString();
};

export const centToCurr = (value) => {
  return numeral(value).divide(100).format("0,0.00");
};

export const currToCents = (value) => {
  if (typeof value === "string") {
    if (value.indexOf(".") < 0) {
      value += "00";
    }
    value = value.replace(/[,.]/g, "");
  }
  return parseInt(value);
};
