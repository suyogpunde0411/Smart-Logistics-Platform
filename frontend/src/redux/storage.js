const createWebStorage = () => {
  return {
    getItem(key) {
      return Promise.resolve(window.localStorage.getItem(key));
    },
    setItem(key, item) {
      window.localStorage.setItem(key, item);
      return Promise.resolve();
    },
    removeItem(key) {
      window.localStorage.removeItem(key);
      return Promise.resolve();
    },
  };
};

export const storage = createWebStorage();
