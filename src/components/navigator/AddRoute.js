import { Form, InputGroup } from "react-bootstrap";
import Button from "react-bootstrap/Button";
import { useState } from "react";
import { addRouteS2 } from "../../utils/apiInteraction";
import toast from "react-hot-toast";
import get from "lodash.get";

export const AddRoute = () => {
  const [fromId, setFromId] = useState("");
  const [toId, setToId] = useState("");
  const addNewRoute = () => {
    toast.promise(
      addRouteS2(
        fromId,
        toId
      ),
      {
        loading: "Добавляем...",
        success: (response) => "Route успешно добавлено!\n\n"+
        "ID: "+response.data.id + "\n" +
        "CreationDate: "+response.data.creationDate + "\n" +
        "Name: "+response.data.name + "\n\n" +
        "FromID: "+response.data.from.id + "\n" +
        "FromX: "+response.data.from.coordinates.x + "\n" +
        "FromY: "+response.data.from.coordinates.y + "\n" +
        "FromName: "+response.data.from.name + "\n\n" +
        "ToID: "+response.data.to.id + "\n" +
        "ToX: "+response.data.to.coordinates.x + "\n" +
        "ToY: "+response.data.to.coordinates.y + "\n" +
        "ToName: "+response.data.to.name + "\n\n" +
        "Distance: "+response.data.distance,
        error: (err) => get(err, "response.data.message", "Error, проверьте поля!"),
      }
    );
  }

  return (
    <Form>
      <div className="optitle mt-3">
        Добавить дорогу между двумя локациями по их ID
      </div>
      <div className="horizontal-placer">
      <Form.Control
          type="number"
          min="1"
          value={fromId}
          placeholder="ID первой локации"
          className="form"
          onChange={(event) => setFromId(event.target.value)}
        />
        <Form.Control
          type="number"
          min="1"
          value={toId}
          placeholder="ID второй локации"
          className="form"
          onChange={(event) => setToId(event.target.value)}
        />
        <Button
          variant="dark"
          className="button-rev-1 form"
          onClick={addNewRoute}
        >Добавить</Button>
      </div>
        
    </Form>
  );
};
