import React from 'react';
import { TouchableOpacity} from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import styles from './styles';

export default class Header extends React.Component {
    render() {
        return (
            <TouchableOpacity
                onPress={() => this.props.buttonAction()}
                style={styles.container}
            >
                <Icon
                    name={this.props.buttonIcon}
                    size={20}
                    color="#FFF"
                />
            </TouchableOpacity>
        );
    }
}