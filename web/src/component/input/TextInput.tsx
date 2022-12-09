import classNames from 'classnames';
import CopyButton from 'component/input/copy/CopyButton';
import InputErrorMessage from 'component/input/errorMessage/InputErrorMessage';
import InputLabel, { InputWidth } from 'component/input/label/InputLabel';
import React, { ChangeEventHandler, FocusEventHandler, useState } from 'react';
import styles from './TextInput.module.scss';

interface Props {
  copyButton?: boolean;
  errorMessage?: string;
  label: string;
  placeholder?: string;
  value: string;
  width?: InputWidth;
  onChange: (newValue: string) => void;
}

const TextInput: React.ForwardRefRenderFunction<HTMLInputElement, Props> =
  ({
    copyButton = false,
    errorMessage = undefined,
    label,
    placeholder = undefined,
    value,
    width = undefined,
    onChange,
  }, ref) => {
    const [isFocused, setIsFocused] = useState(false);

    const handleFocus: FocusEventHandler<HTMLInputElement> = () => setIsFocused(true);

    const handleBlur: FocusEventHandler<HTMLInputElement> = () => setIsFocused(false);

    const handleChange: ChangeEventHandler<HTMLInputElement> = (event) => {
      onChange(event.target.value);
    };

    return (
      <InputLabel label={label} width={width}>
        <div className={classNames(styles.container, { focus: isFocused, [styles.error]: Boolean(errorMessage) })}>
          <input
            ref={ref}
            className={classNames(styles.input, { [styles.copyable]: copyButton })}
            placeholder={placeholder}
            value={value}
            onBlur={handleBlur}
            onChange={handleChange}
            onFocus={handleFocus}
          />
          {copyButton ? <CopyButton onCopy={() => value} /> : null}
        </div>
        <InputErrorMessage>{errorMessage}</InputErrorMessage>
      </InputLabel>
    );
  };

export default React.forwardRef(TextInput);
