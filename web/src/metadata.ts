export const getRootHost = (): string => window.location.host;

export const getRootUrl = (): string => `${window.location.protocol}//${getRootHost()}`;

export const getHref = (): string => `${window.location.pathname}${window.location.search}${window.location.hash}`;
