import React from 'react'
import NumberFormat from 'react-number-format';

export default ({ inputRef, onChange, ...other }) => {
    return (
        <NumberFormat
            {...other}
            getInputRef={inputRef}
            onValueChange={values => {
                onChange({
                    target: {
                        value: values.value,
                    },
                });
            }}
            decimalScale='2'
            fixedDecimalScale
            thousandSeparator
        />
    );
}