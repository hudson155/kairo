module.exports = {
  root: true,
  plugins: [
    'react',
    'import',
  ],
  extends: [
    'react-app',
    'eslint:recommended',
    'plugin:react/recommended',
    'plugin:import/errors',
  ],
  rules: {
    'react/no-unescaped-entities': 'off',
    'import/exports-last': 'error',
    'import/first': 'error',
    'import/newline-after-import': 'error',
    'import/no-duplicates': 'error',
    'import/no-self-import': 'error',
    'import/order': ['error', {
      'alphabetize': { 'order': 'asc', 'caseInsensitive': true },
      'newlines-between': 'always',
    }],
    'indent': ['error', 2],
    'react/function-component-definition': 'error',
    'react/jsx-closing-bracket-location': 'error',
    'react/jsx-closing-tag-location': 'error',
    'react/jsx-curly-brace-presence': ['error', { 'props': 'never', 'children': 'never' }],
    'react/jsx-first-prop-new-line': ['error', 'multiline'],
    'react/jsx-indent-props': ['error', 2],
    'react/jsx-indent': ['error', 2],
    'react/jsx-pascal-case': 'error',
    'react/jsx-props-no-multi-spaces': 'error',
    'react/jsx-sort-props': 'error',
    'react/jsx-wrap-multilines': 'error',
    'react/no-array-index-key': 'error',
    'react/no-unsafe': 'error',
    'react/no-string-refs': 'error',
    'react/prop-types': 'error',
    'react/sort-prop-types': 'error',
  },
  overrides: [
    {
      files: ['**/*.ts?(x)'],
      parser: '@typescript-eslint/parser',
      plugins: [
        '@typescript-eslint',
      ],
      extends: [
        'eslint:recommended',
        'plugin:@typescript-eslint/recommended',
        'plugin:import/typescript',
      ],
      rules: {
        '@typescript-eslint/camelcase': 'off',
        '@typescript-eslint/comma-spacing': ['error'],
        '@typescript-eslint/consistent-type-definitions': 'error',
        '@typescript-eslint/explicit-function-return-type': 'off',
        'comma-spacing': 'off',
      },
    },
  ],
  settings: {
    'import/resolver': {
      typescript: {},
    },
  },
};
