/* eslint-disable no-unused-vars */
import {
  Box,
  Button,
  CircularProgress,
  Container,
  FormControl,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  TextField,
  Typography,
} from "@mui/material";
import axios from "axios";
import { useState } from "react";

function App() {
  const [loading, setLoading] = useState(false);
  const [originalEmailContent, setOriginalEmailContent] = useState("");
  const [tone, setTone] = useState("Formal");
  const [generatedReply, setGeneratedReply] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async () => {
    setLoading(true);
    setError("");
    try {
      const response = await axios.post(
        "http://localhost:8080/api/email/generate",
        {
          originalEmailContent,
          tone,
        }
      );
      setGeneratedReply(
        typeof response.data === "string"
          ? response.data
          : JSON.stringify(response.data)
      );
    } catch (error) {
      setError("Failed to generate the email reply");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container
      maxWidth="md"
      sx={{ bgcolor: "#f5f5f5", minHeight: "100vh", py: 5 }}
    >
      <Paper elevation={3} sx={{ p: 4, borderRadius: 3 }}>
        <Typography variant="h4" align="center" gutterBottom fontWeight={600}>
          Smart Email Assistant
        </Typography>
        <Box>
          <TextField
            fullWidth
            multiline
            rows={6}
            variant="outlined"
            label="Original Email Content"
            value={originalEmailContent}
            onChange={(e) => setOriginalEmailContent(e.target.value)}
            sx={{ mb: 3 }}
          />
          <FormControl fullWidth sx={{ mb: 3 }}>
            <InputLabel>Reply Type (Optional)</InputLabel>
            <Select
              value={tone}
              label="Reply Type (Optional)"
              onChange={(event) => setTone(event.target.value)}
            >
              <MenuItem value="">None</MenuItem>
              <MenuItem value="professional">Professional</MenuItem>
              <MenuItem value="casual">Casual</MenuItem>
              <MenuItem value="friendly">Friendly</MenuItem>
            </Select>
          </FormControl>
          <Button
            variant="contained"
            color="primary"
            onClick={handleSubmit}
            disabled={!originalEmailContent || loading}
            fullWidth
            sx={{ py: 1.5, fontSize: "1rem", fontWeight: 600 }}
          >
            {loading ? <CircularProgress size={24} /> : "Generate Reply"}
          </Button>
        </Box>

        {error && (
          <Typography color="error" sx={{ mt: 2 }}>
            {error}
          </Typography>
        )}

        {generatedReply && (
          <Box sx={{ mt: 4 }}>
            <Typography variant="h6" gutterBottom fontWeight={600}>
              Generated Reply:
            </Typography>
            <TextField
              fullWidth
              multiline
              rows={6}
              variant="outlined"
              value={generatedReply}
              aria-readonly
              sx={{ mb: 2 }}
            />
            <Button
              variant="outlined"
              color="secondary"
              fullWidth
              sx={{ py: 1.2, fontWeight: 600 }}
              onClick={() => navigator.clipboard.writeText(generatedReply)}
            >
              Copy To Clipboard
            </Button>
          </Box>
        )}
      </Paper>
    </Container>
  );
}

export default App;
