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
  const env = process.env.ENV || 'development';
  switch (env) {
    case 'development':
      return {
        API_ROOT_URL: JSON.stringify('http://localhost:55100'),
        AUTH0_DOMAIN: JSON.stringify('limber.auth0.com'),
        COPYRIGHT_HOLDER: JSON.stringify('12308932 Canada Ltd.'),
        SEGMENT_WRITE_KEY: JSON.stringify(null),
      };
    case 'production':
      return {
        API_ROOT_URL: JSON.stringify('https://api.limberapp.io'),
        AUTH0_DOMAIN: JSON.stringify('limber.auth0.com'),
        COPYRIGHT_HOLDER: JSON.stringify('12308932 Canada Ltd.'),
        SEGMENT_WRITE_KEY: JSON.stringify('llKAtlzOykmSneKOSTNgWJGoNUTuLpCH'),
      };
    default:
      throw new Error(`Unknown DEPLOYMENT_ENV: ${env}.`);
  }
}
