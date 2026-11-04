
const express = require("express");
const path = require("path");

const app = express();

app.use(express.json());
app.use(express.static(path.join(__dirname, "public")));

app.get("/api/status", (req, res) => {
  res.json({
    status: "online",
    project: "Hisaab Calculator"
  });
});

app.listen(3000, "0.0.0.0", () => {
  console.log("Hisaab Calculator server running on port 3000");
});

