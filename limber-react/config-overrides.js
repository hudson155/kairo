const { addBabelPlugins, addBabelPreset, override } = require('customize-cra');

module.exports = override(
  addBabelPlugins(
    'babel-plugin-relay',
  ),
  addBabelPreset('@emotion/babel-preset-css-prop'),
);
