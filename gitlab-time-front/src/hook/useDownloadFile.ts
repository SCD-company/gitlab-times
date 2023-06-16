import { useCallback, useMemo } from 'react';

export const useDownloadFile = (contentType: string) => {
  const downloadElement = useMemo(() => {
    const element = document.createElement('a');
    element.style.display = 'none';
    return element;
  }, []);

  return useCallback(
    (filename: string, text: any) => {
      downloadElement.setAttribute('href', 'data:' + contentType + ';base64,' + encodeURIComponent(text));
      downloadElement.setAttribute('download', filename);
      downloadElement.click();
    },
    [downloadElement, contentType]
  );
};
