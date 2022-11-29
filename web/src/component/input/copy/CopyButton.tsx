import { Transition } from '@headlessui/react';
import classNames from 'classnames';
import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import Text from 'component/text/Text';
import React, { Fragment, MouseEventHandler, useState } from 'react';
import { durationFlash } from 'style/theme';
import { transitions } from 'style/transitions';
import styles from './CopyButton.module.scss';

const COPY_MESSAGE = `Copy`;
const COPY_SUCCESS_MESSAGE = `Copied!`;
const COPY_FAILURE_MESSAGE = `Something went wrong!`;

interface Props {
  onCopy: () => string;
}

/**
 * When pressed, calls [onCopy] and copies the resulting content to the clipboard.
 * A tooltip will show when the button is focused or hovered.
 * The tooltip's content and color reflects the state of the copy operation.
 */
const CopyButton: React.FC<Props> = ({ onCopy }) => {
  const [isFocused, setIsFocused] = useState(false);
  const [isHovered, setIsHovered] = useState(false);

  const [message, setMessage] = useState<string>(COPY_MESSAGE);
  const [messageTimeout, setMessageTimeout] = useState<ReturnType<typeof setTimeout>>();

  const [messageClassName, setMessageClassName] = useState<string>();
  const [messageClassNameTimeout, setMessageClassNameTimeout] = useState<ReturnType<typeof setTimeout>>();

  const handleCopy: MouseEventHandler<HTMLButtonElement> = () => void (async () => {
    try {
      await navigator.clipboard.writeText(onCopy()); // eslint-disable-line compat/compat
      setMessage(COPY_SUCCESS_MESSAGE);
      setMessageTimeout(setTimeout(() => setMessage(COPY_MESSAGE), 3000));
      setMessageClassName(styles.success);
      setMessageClassNameTimeout(setTimeout(() => setMessageClassName(undefined), 2 * durationFlash));
    } catch (e) {
      console.error(`Copy failed`, e);
      setMessage(COPY_FAILURE_MESSAGE);
      setMessageTimeout(setTimeout(() => setMessage(COPY_MESSAGE), 5000));
      setMessageClassName(styles.failure);
      setMessageClassNameTimeout(setTimeout(() => setMessageClassName(undefined), 5000));
    } finally {
      clearTimeout(messageTimeout);
      clearTimeout(messageClassNameTimeout);
    }
  })();

  return (
    <Button
      className={styles.button}
      variant="unstyled"
      onBlur={() => setIsFocused(false)}
      onClick={handleCopy}
      onFocus={() => setIsFocused(true)}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      <Icon name="content_copy" size="small" />
      <Transition
        as={Fragment}
        show={Boolean(isFocused || isHovered)}
        {...transitions(`fadeIn`, `moveUpIn`, `fadeOut`, `moveDownOut`)}
      >
        <div className={classNames(styles.tooltipContainer, messageClassName)}>
          <Text className={styles.tooltip} size="small">
            {message}
          </Text>
        </div>
      </Transition>
    </Button>
  );
};

export default CopyButton;
