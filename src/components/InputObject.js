import { Form, InputGroup } from "react-bootstrap";
import { InputField } from "./InputField";
import { useRecoilState } from "recoil";
import { bufferRoute } from "../state/atoms";
import get from "lodash.get";

export const InputObject = ({ id, fields }) => {
  const [route, setRoute] = useRecoilState(bufferRoute);

  return (
    <>
      <Form.Label htmlFor={id}>{id.firstLetterToUppercase()}</Form.Label>
      <InputGroup className="mb-3" id={id}>
        {fields.map((field) => (
          <InputField
            key={id + "." + field}
            id={id + "." + field}
            isEmbedded={true}
            type={field === "name" ? "text" : "number"}
          />
        ))}
      </InputGroup>
    </>
  );
};
