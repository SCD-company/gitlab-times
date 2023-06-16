import { Styled as S } from './CheckboxUseSpentAt.styled';

export interface CheckboxUseSpentAtProps {
  value: boolean;
  setValue: (value: boolean) => void;
}

export const CheckboxUseSpentAt: React.FC<CheckboxUseSpentAtProps> = ({ value, setValue }) => {
  return (
    <S.FullWidthContainer>
      <S.Label>
        <p>Other</p>
        <hr />
      </S.Label>
      <S.Checkbox>
        <input type="checkbox" checked={value} onChange={(e) => setValue(e.target.checked)} />
        <label>Use spent time</label>
      </S.Checkbox>
    </S.FullWidthContainer>
  );
};
