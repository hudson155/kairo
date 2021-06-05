import { CSSObject, Theme } from '@emotion/react';

const styles = {
  root: (theme: Theme): CSSObject => ({
    backgroundColor: theme.color.grey50,
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    padding: theme.size.$16,
    boxShadow: `0 0 ${theme.size.$4} ${theme.color.grey600}`,
    fontSize: theme.size.$16,
    fontWeight: 'bold',
  }),
  left: (theme: Theme): CSSObject => ({
    flexGrow: 1,
    display: 'flex',
    alignItems: 'flex-start',
    '> *:not(:last-child)': {
      marginRight: theme.size.$12,
    },
  }),
  right: (theme: Theme): CSSObject => ({
    display: 'flex',
    alignItems: 'center',
    flexDirection: 'row-reverse',
    '> *:not(:first-child)': {
      marginRight: theme.size.$12,
    },
  }),
  activeLink: (theme: Theme): CSSObject => ({
    borderBottom: `${theme.size.$2} solid ${theme.color.special.copper}`,
  }),
};

export default styles;
