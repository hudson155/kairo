const webpack = require('webpack');

/**
 * Use history API fallback to allow loading non-root paths.
 */

if (config.devServer) {
  config.devServer.historyApiFallback = true;
}

/**
 * Configure compile-time environment variable substitution.
 */

const environmentVariables = Object.entries(getEnvironmentVariables()).reduce((result, [key, value]) => {
  result[`process.env.${key}`] = value;
  return result;
}, {});

config.plugins.push(new webpack.DefinePlugin(environmentVariables));

function getEnvironmentVariables() {
  const nodeEnv = process.env.NODE_ENV || 'development';
  switch (nodeEnv) {
    case 'development':
      return {
        API_ROOT_URL: JSON.stringify('http://localhost:55100'),
      };
    case 'production':
      return {
        API_ROOT_URL: JSON.stringify('https://api.limberapp.io'),
      };
    default:
      throw new Error(`Unknown NODE_ENV: ${nodeEnv}.`);
  }
}
