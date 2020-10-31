// Version de solidity a utilizar
pragma solidity ^0.4.24;

contract ShadoomToken {

    // Libreria para ejecutar operaciones aritmeticas con validacion de Overflow/Underflow
    using SafeMath for uint256;

    // Suministro total te tokens disponibles en el mercado
    uint256 totalSupply_;

    // Propietario del contrato
    address private owner;

    // Nombre del token
    string public constant name = "Shadoom";

    // Simbolo del token
    string public constant symbol = "SHDM";

    // Numero de decimales del token
    uint8 public constant decimals = 18;

    // Equivalencia token y wei
    uint256 tokenWeiValue = 1000;

    /**
    *    @dev Constructor que se ejecuta cuando se despliega el contrato, inicializando el numero total de tokens y se transfieren al propietario del contrato
    *    @param total Suministro total te tokens disponibles en el mercado
    */
    constructor(uint256 total) public {
        totalSupply_ = total;
        balances[msg.sender] = totalSupply_;
        owner = msg.sender;
    }

    // Almacena el balance de tokens de los propietarios de las cuentas
    mapping(address => uint256) balances;
    // Almacena todas las cuentas que pueden retirar tokens de otras cuentas y que cantidad
    mapping(address => mapping(address => uint256)) allowed;

    /**
    *   @title totalSupply
    *
    *   @return uint con la cantidad de tokens disponibles en el mercado
    */
    function totalSupply() public view returns (uint256) {
        return totalSupply_;
    }

    /**
    *   @title balanceOf
    *
    *   @param tokenOwner direccion que se quiere consultar el balance
    *   @return uint con la cantidad de tokens de la direccion
    */
    function balanceOf(address tokenOwner) public view returns (uint256) {
        return balances[tokenOwner];
    }

    /**
    *   @title allowance
    *
    *   @param tokenOwner Direccion que permite a 'spender' retirar tokens de su propiedad
    *   @param spender Direccion que puede retirar tokens propiedad de 'tokenOwner'
    *   @return uint Con la cantidad de tokens propiedad de 'tokenOwner' que 'spender' puede retirar
    *
    */
    function allowance(address tokenOwner, address spender) public view returns (uint256) {
        return allowed[tokenOwner][spender];
    }

    /**
    *   @title transfer
    *   
    *   @param to direccion a la cual el ejecutor de la transaccion quiere trasnferir tokens
    *   @param tokens cantidad de tokens que se quieren trasnferir
    *   @return bool Indica si la trasnferencia se realizo correctamente
    */
    function transfer(address to, uint tokens) public returns (bool) {
        return transferTokens(msg.sender,to, tokens);
    }

    /**
    *   @title approve
    *
    *   @param spender direccion a la cual el ejecutor de la transaccion quiere permitir retirar sus tokens
    *   @param tokens cantidad de tokens que el ejecutor de la transaccion quiere permitir retirar a 'spender'
    *   @return bool Indica si la operacion se realizo correctamente
    */
    function approve(address spender, uint tokens) public returns (bool) {
        allowed[msg.sender][spender] = tokens;

        emit Approval(msg.sender, spender, tokens);

        return true;
    }

    /**
    *   @title transferFrom
    *
    *   @param from Direccion de la cual se van a tomar los tokens a transferir
    *   @param to Direccion a la cual se van a transferir los tokens
    *   @param tokens cantidad de tokens a transferir
    *   @return bool Indica si la trasnferencia se realizo correctamente
    *
    */
    function transferFrom(address from, address to, uint tokens) public returns (bool) {
        // Valida que la direccion a la cual se quieren transferir tokens tenga perimitida dicha cantidad disponible
        require (tokens <= allowance(from, to));
        // Valida que la direccion de la cual se quieren transferir tokens tenga disponible dicha cantidad disponible
        require (tokens <= balanceOf(from));

        balances[from] = balances[from].sub(tokens);
        allowed[from][to] = allowed[from][to].sub(tokens);

        balances[to] = balances[to].add(tokens);
        
        emit Transfer(from, to, tokens);

        return true;
    }

    /**
    *   @title buy
    *
    *   @param tokens cantidad de tokens que se quieren comprar
    *   @return bool Indica si la compra se realizo correctamente
    */
    function buy(uint tokens) public payable returns (bool) {
        // Validacion de seguridad
        // Evita que se puedan 'comprar' tokens por 0 ETH cuando el numero de tokens sea menor al valor del token en wei
        require(tokens >= tokenWeiValue);
        // Valida que el Ether enviado sea igual al valor correcto.
        require(msg.value == tokens / tokenWeiValue);

        return transferTokens(owner, msg.sender, tokens);
    }

    /**
    *   @title burn
    *
    *   @param tokens cantidad de tokens que se quieren quemar
    *   @return bool Indica si la operacion se realizo correctamente
    */
    function burn(uint tokens) public returns (bool) {
        // Valida que los tokens a quemar sean menores o iguales a total disponible
        require(tokens <= totalSupply_);
        if (transferTokens(msg.sender, 0x0000000000000000000000000000000000000000, tokens)){
            totalSupply_ = totalSupply_.sub(tokens);
            return true;
        }
        return false;
    }

    /**
    *   @title transferTokens
    *
    *   @param sender Direccion de la cual se van a tomar los tokens a transferir
    *   @param to Direccion a la cual se van a transferir los tokens
    *   @param tokens cantidad de tokens a transferir
    *   @return bool Indica si la trasnferencia se realizo correctamente
    *
    */
    function transferTokens(address sender, address to, uint256 tokens) private returns (bool) {
        // Valida que la direccion de la cual se quieren transferir tokens tenga disponible dicha cantidad disponible
        require(tokens <= balanceOf(sender));
        balances[sender] = balances[sender].sub(tokens);
        balances[to] = balances[to].add(tokens);

        emit Transfer(sender, to, tokens);

        return true;
    }
    
    /**
    *   @title checkPrice
    *   @dev Valida el precio en wei de los tokens recibidos
    *
    *   @param neededTokens cantidad de tokens de la cual se quiere validar el precio en wei
    */
    function checkPrice(uint256 neededTokens) public view returns (uint256){
        return  neededTokens.div(tokenWeiValue);
    }


    /**
    *   @dev evento que se emite cuando se realiza una aprobacion de tokens
    */
    event Approval(address indexed tokenOwner, address indexed spender, uint tokens);
    /**
    *   @dev evento que se emite cuando se realiza una transferencia de tokens
    */
    event Transfer(address indexed from, address indexed to, uint tokens);

}

/**
 * @title SafeMath
 * @dev Librería para ejecutar operaciones aritmeticas con validacion de Overflow/Underflow
 */
library SafeMath { 
    function sub(uint256 a, uint256 b) internal pure returns (uint256) {
      require(b <= a, "SafeMath: subtraction overflow");
      
      return a - b;
    }
    
    function add(uint256 a, uint256 b) internal pure returns (uint256) {
      uint256 c = a + b;
      require(c >= a, "SafeMath: addition overflow");
      
      return c;
    }

    function div(uint256 a, uint256 b) internal pure returns (uint256) {
        require(b > 0, "SafeMath: division by zero");
        
        return a / b;
    }
}