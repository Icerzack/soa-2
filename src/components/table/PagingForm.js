import { Form, InputGroup } from "react-bootstrap";
import Button from "react-bootstrap/Button";
import { useEffect, useState } from "react";
import { useRecoilState, useSetRecoilState } from "recoil";
import get from "lodash.get";
import { isDataNeedsToBeUpdatedState, pagingState } from "../../state/atoms";

export const PagingForm = () => {
  const [paging, setPaging] = useRecoilState(pagingState);
  const setIsDataNeedsToBeUpdated = useSetRecoilState(
    isDataNeedsToBeUpdatedState
  );
  const [elementsCount, setElementsCount] = useState("");
  const [pageNumber, setPageNumber] = useState("");
  const [disable, setDisable] = useState(true);

  const click = () => {
    const newPaging = {};
    if (elementsCount.length) newPaging.elementsCount = Number(elementsCount);
    if (pageNumber.length) newPaging.pageNumber = Number(pageNumber);
    setPaging(newPaging);
    setIsDataNeedsToBeUpdated(true);
  };

  useEffect(() => {
    (elementsCount === "" && pageNumber === "")
      ? setDisable(true)
      : setDisable(false);
  }, [elementsCount, pageNumber]);

  return (
    <Form id="pagingForm">
      <div className="horizontal-placer">
        <Form.Control
          type="number"
          min="1"
          placeholder="Лимит"
          className="form"
          onChange={(event) => {
            setElementsCount(event.target.value);
          }}
        />
        <Form.Control
          type="number"
          min="1"
          placeholder="Номер страницы"
          className="form"
          onChange={(event) => {
            setPageNumber(event.target.value);
          }}
        />
        <Button
          className="button-3 form"
          onClick={click}
          disabled={disable}
        >
          Применить
        </Button>
      </div>
    </Form>
  );
};
