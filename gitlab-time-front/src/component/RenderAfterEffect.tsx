import { PropsWithChildren, ReactElement, useEffect, useState } from 'react';

export interface RenderAfterEffectProps {
  effect: () => void;
  children: ReactElement;
}

export const RenderAfterEffect: React.FC<PropsWithChildren<RenderAfterEffectProps>> = ({ effect, children }) => {
  const [iDidIt, setIDidIt] = useState(false);
  useEffect(() => {
    effect();
    setIDidIt(true);
  }, [effect]);

  return iDidIt ? children : <></>;
};
