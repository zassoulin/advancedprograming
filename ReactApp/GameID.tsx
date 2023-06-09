import React, {useState} from 'react';
import {
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import Section from './Section';
import Settings from './Settings';

const GameID = (props: {
  setGottem: React.Dispatch<React.SetStateAction<boolean>>;
  setResponse: React.Dispatch<React.SetStateAction<string>>;
}) => {
  const {setGottem, setResponse} = props;

  const [inputValue, setInputValue] = useState('');

  const handleSubmitText = async () => {
    console.log('Input value:', inputValue);

    const hostname = await Settings.get().loadHostnameSetting();
    const fetchRequest = `http://${hostname}/score-table.json?gameId=${inputValue}`;

    try {
      console.log(`Trying to fetch the score table: "${fetchRequest}"`);
      const response = await fetch(fetchRequest)
        .then(function (resp) {
          return resp.text();
        })
        .then(function (message) {
          return message;
        });

      console.log('Res: ', response);
      setResponse(response);
      setGottem(true);
    } catch (error) {
      console.error('Failed to fetch the score table:');
      console.error(error);
    }
  };

  const handleInputChange = (text: string) => {
    setInputValue(text);
  };

  return (
    <Section title="Enter Game ID">
      <View style={styles.container}>
        <TextInput
          style={styles.input}
          value={inputValue}
          onChangeText={handleInputChange}
          onSubmitEditing={handleSubmitText}
          placeholder="Game ID"
        />
        <TouchableOpacity onPress={handleSubmitText} style={styles.button}>
          <Text style={styles.buttonLabel}>Confirm</Text>
        </TouchableOpacity>
      </View>
    </Section>
  );
};

const styles = StyleSheet.create({
  container: {
    display: 'flex',
  },
  button: {
    padding: 10,
    backgroundColor: 'blue',
    borderRadius: 5,
    marginBottom: 10,
  },
  buttonLabel: {
    color: 'white',
    fontSize: 16,
  },
  input: {
    borderWidth: 1,
    borderColor: 'gray',
    borderRadius: 5,
    padding: 10,
    marginTop: 10,
    marginBottom: 10,
    width: 200,
    color: 'black',
  },
});

export default GameID;
